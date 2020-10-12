package com.trelp.imgur.presentation

import com.jakewharton.rxrelay2.PublishRelay
import com.trelp.imgur.data.SchedulersProvider
import io.reactivex.Observable
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

object Paginator {

    /**
     * Action - события извне (неважно откуда), которые пытаются влиять на данные.
     */
    sealed class Action {
        // Со стороны UI
        object Refresh : Action()
        object LoadMore : Action()
        object Restart : Action()

        // Со стороны Model
        data class NewPage<T>(val page: Int, val items: List<T>) : Action()
        data class NewEmptyPage<T>(val page: Int, val items: List<T>) : Action()
        data class PageError(val error: Throwable) : Action()
    }

    /**
     * State - отличающийся набор данных. Переходы между States происходят из-за Actions.
     * Из State A, по конкретному Action, можно перейти только в State B.
     */
    sealed class State {
        object Empty : State()
        object EmptyProgress : State()
        data class EmptyError(val error: Throwable) : State()
        data class Data<T>(val page: Int, val data: List<T>) : State()
        data class NewPageProgress<T>(val page: Int, val data: List<T>) : State()
        data class FullData<T>(val page: Int, val data: List<T>) : State()
        data class Refresh<T>(val page: Int, val data: List<T>) : State()
        data class FullDataRefresh<T>(val page: Int, val data: List<T>) : State()
    }

    /**
     * SideEffect - это такой процесс, который позволяет кидать Actions не с помощью
     * пользовательского взаимодействия, а с помощью Model и т.п. SideEffect делает, например,
     * какой-то call на сервер и результат этого call летит в виде Action.
     *
     * SideEffects (важный для нас момент) сообщают о том, что был переход между States и если этот
     * переход нас интересует, его можно как-то обработать, например запустить rx.Single или
     * Coroutine и по результату этого асинхронного процесса будет новый Action.
     * Т.е. это callback что был переход между состояниями.
     *
     * Presenter запускает SideEffects.
     */
    sealed class SideEffect {
        data class LoadPage(val page: Int) : SideEffect()
        data class ErrorEvent(val error: Throwable) : SideEffect()
    }

    private fun <T> reducer(
        action: Action,
        state: State,
        sideEffectListener: (SideEffect) -> Unit
    ): State = when (state) {
        is State.Empty -> {
            when (action) {
                is Action.Refresh -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.EmptyProgress -> {
            when (action) {
                is Action.NewPage<*> -> State.Data(0, action.items as List<T>)
                is Action.NewEmptyPage<*> -> State.Empty
                is Action.PageError -> State.EmptyError(action.error)
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.EmptyError -> {
            when (action) {
                is Action.Refresh -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.Data<*> -> {
            when (action) {
                // На экране уже отображаются данные и поверх них крутится progress.
                is Action.Refresh -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.Refresh(state.page, state.data as List<T>)
                }
                is Action.LoadMore -> {
                    sideEffectListener(SideEffect.LoadPage(state.page + 1))
                    State.NewPageProgress(state.page, state.data as List<T>)
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.NewPageProgress<*> -> {
            when (action) {
                is Action.Refresh -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.Refresh(state.page, state.data as List<T>)
                }
                is Action.NewEmptyPage<*> -> State.FullData(state.page, state.data as List<T>)
                is Action.NewPage<*> -> State.Data(
                    state.page + 1,
                    state.data as List<T> + action.items as List<T>
                )
                // Отображается ошибка, но данные остаются на экране.
                is Action.PageError -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.error))
                    State.Data(state.page, state.data as List<T>)
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.FullData<*> -> {
            when (action) {
                // Почему не в Refresh, а в новый State?
                // Потому что, если пройтись по цепочке FullData -> Refresh -> произошла ошибка и
                // попали в Data, и user проскролит до низа, то срабоатет загрузка следующей страницы,
                // хотя пагинация уже не нужна.
                is Action.Refresh -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.FullDataRefresh(state.page, state.data as List<T>)
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.Refresh<*> -> {
            when (action) {
                is Action.NewEmptyPage<*> -> State.Empty
                is Action.NewPage<*> -> State.Data(0, action.items as List<T>)
                // Отображается ошибка, но данные остаются на экране.
                is Action.PageError -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.error))
                    State.Data(state.page, state.data as List<T>)
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
        is State.FullDataRefresh<*> -> {
            when (action) {
                is Action.NewEmptyPage<*> -> State.Empty
                is Action.NewPage<*> -> State.Data(0, action.items as List<T>)
                is Action.PageError -> {
                    sideEffectListener(SideEffect.ErrorEvent(action.error))
                    State.FullData(state.page, state.data as List<T>)
                }
                is Action.Restart -> {
                    sideEffectListener(SideEffect.LoadPage(0))
                    State.EmptyProgress
                }
                else -> state
            }
        }
    }

    class Store<T> @Inject constructor(schedulers: SchedulersProvider) {
        private var currentState: State = State.Empty

        // SideEffects должны уходить асинхронно в другой Thread. Иначе можно заблокировать
        // sideEffectListener и пока он не завершится, новый State не будет возвращен.
        // Тот кто подписывается, будет получать callbacks на другом Thread.
        private val sideEffectsExecutor: ExecutorService = Executors.newSingleThreadExecutor()
        // SideEffects запускаются не на UI Thread, а результат нужен на UI.
        // В случае c SideEffect.LoadPage, имеется rx chain в presenter в котором также происходит
        // отписка от предыдущего SideEffect.LoadPage.
        // В случае с SideEffect.ErrorEvent сообщение летит с другого треда на View и никакой
        // обработки SideEffect нету.
        private val sideEffectsRelay: PublishRelay<SideEffect> = PublishRelay.create()

        val sideEffects: Observable<SideEffect> =
            sideEffectsRelay
                .hide()
                .observeOn(schedulers.ui())

        var render: (State) -> Unit = {}
            set(value) {
                field = value
                value(currentState)
            }

        fun proceed(action: Action) {
            Timber.d("Action: $action")
            val newState = reducer<T>(action, currentState) {
                Timber.d("SideEffect: $it")
                sideEffectsExecutor.submit { sideEffectsRelay.accept(it) }
            }
            if (newState != currentState) {
                currentState = newState
                Timber.d("State: $currentState")
                render(currentState)
            }
        }
    }
}