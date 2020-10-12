package com.trelp.imgur.ui.drawer

import android.os.Bundle
import android.view.View
import com.trelp.imgur.R
import com.trelp.imgur.databinding.FragmentNavDrawerBinding
import com.trelp.imgur.databinding.ItemAddAccountBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.drawer.NavDrawerComponent
import com.trelp.imgur.di.flow.drawer.DrawerFlowComponent
import com.trelp.imgur.domain.session.UserAccount
import com.trelp.imgur.inflater
import com.trelp.imgur.presentation.drawer.NavDrawerPresenter
import com.trelp.imgur.presentation.drawer.NavDrawerView
import com.trelp.imgur.presentation.drawer.NavDrawerView.MenuItem
import com.trelp.imgur.presentation.drawer.NavDrawerView.MenuItem.*
import com.trelp.imgur.ui.base.BaseFragment
import moxy.ktx.moxyPresenter
import javax.inject.Inject
import javax.inject.Provider

class NavDrawerFragment : BaseFragment<NavDrawerComponent>(R.layout.fragment_nav_drawer),
    NavDrawerView {

    private val binding
        get() = viewBinding!! as FragmentNavDrawerBinding

    @Inject
    lateinit var presenterProvider: Provider<NavDrawerPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentNavDrawerBinding.bind(view)

        showAccounts(false)

        with(binding) {
            homeMenuItem.tag = HOME
            settingsMenuItem.tag = SETTINGS
            aboutMenuItem.tag = ABOUT

            logoutIv.setOnClickListener { presenter.onLogoutClicked() }
            dropDownIv.setOnClickListener { showAccounts(accountsContainer.visibility == View.GONE) }

            homeMenuItem.setOnClickListener { presenter.onMenuItemClicked(it.tag as MenuItem) }
            settingsMenuItem.setOnClickListener { presenter.onMenuItemClicked(it.tag as MenuItem) }
            aboutMenuItem.setOnClickListener { presenter.onMenuItemClicked(it.tag as MenuItem) }
        }
    }
    //endregion

    override fun setAccounts(accounts: List<UserAccount>, currentAccount: UserAccount) {
        binding.avatarIv.setOnClickListener { presenter.onProfileClicked() }

        with(binding.accountsContainer) {
            removeAllViews()

            accounts.forEach { acc ->
                ItemAddAccountBinding.inflate(inflater, this, false)
                    .apply {
                        avatarIv.setImageResource(R.drawable.fake_profile_avatar_image)
                        usernameTv.text = acc.username
                        root.setOnClickListener { presenter.onAccountClicked(acc) }
                    }
                    .also { addView(it.root) }
            }

            ItemAddAccountBinding.inflate(inflater, this, false)
                .apply { root.setOnClickListener { presenter.onAddAccountClicked() } }
                .also { addView(it.root) }
        }
    }

    override fun selectMenuItem(item: MenuItem) {
        with(binding.navMenuContainer) {
            (0 until childCount)
                .map { getChildAt(it) }
                .forEach { menuItem ->
                    menuItem.tag?.let {
                        menuItem.isSelected = it as MenuItem == item
                    }
                }
        }
    }

    private fun showAccounts(show: Boolean) {
        with(binding) {
            if (show) {
                dropDownIv.rotation = 180F
                accountsContainer.visibility = View.VISIBLE
            } else {
                dropDownIv.rotation = 0F
                accountsContainer.visibility = View.GONE
            }
        }
    }

    fun onScreenChanged(item: MenuItem) {
        presenter.onScreenChanged(item)
    }

    //region Dagger
    override fun setupComponent(componentKey: String) {
        Injector.getComponent(this, componentKey).inject(this)
    }

    override fun createComponent() =
        Injector.findComponent<DrawerFlowComponent>().navDrawerComponentFactory().create()
    //endregion
}