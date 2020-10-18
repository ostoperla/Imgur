package com.trelp.imgur.ui.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.trelp.imgur.R
import com.trelp.imgur.databinding.FragmentAuthBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.auth.AuthComponent
import com.trelp.imgur.di.flow.auth.AuthFlowComponent
import com.trelp.imgur.presentation.auth.AuthPresenter
import com.trelp.imgur.presentation.auth.AuthView
import com.trelp.imgur.ui.addSystemTopPadding
import com.trelp.imgur.ui.base.BaseFragment
import com.trelp.imgur.visible
import moxy.ktx.moxyPresenter
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class AuthFragment : BaseFragment<AuthComponent>(R.layout.fragment_auth), AuthView {

    private val binding
        get() = viewBinding!! as FragmentAuthBinding

    @Inject
    lateinit var presenterProvider: Provider<AuthPresenter>
    private val presenter by moxyPresenter { presenterProvider.get() }

    //region LifeCycle
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentAuthBinding.bind(view)

        with(binding) {
            toolbar.addSystemTopPadding()
            toolbar.setNavigationOnClickListener { presenter.onBackPressed() }
            emptyView.setRefreshListener { presenter.refresh() }
        }

        clearCookies()
        configWebView()
    }

    override fun onResume() {
        super.onResume()

        binding.authWebView.onResume()
    }

    override fun onPause() {
        super.onPause()

        binding.authWebView.onPause()
    }

    override fun onDestroyView() {
        binding.authWebView.destroy()

        super.onDestroyView()
    }
    //endregion

    override fun loadUrl(url: String) {
        binding.authWebView.loadUrl(url)
    }

    override fun showProgress(isVisible: Boolean) {
        Toast.makeText(requireContext(), "$isVisible", Toast.LENGTH_SHORT).show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configWebView() {
        with(binding.authWebView) {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {

                    Timber.d("onPageFinished")

                    super.onPageFinished(view, url)

                    showProgress(false)
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                    Timber.d("onPageStarted")

                    showProgress(true)
                    showEmptyView(false)

                    super.onPageStarted(view, url, favicon)
                }

                @RequiresApi(Build.VERSION_CODES.M)
                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {

                    Timber.d("onReceivedError")

                    super.onReceivedError(view, request, error)

                    showEmptyView(true)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {

                    Timber.d("shouldOverrideUrlLoading")

                    return presenter.onRedirect(url)
                }

                @RequiresApi(value = Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {

                    Timber.d("shouldOverrideUrlLoading N")

                    return presenter.onRedirect(request.url.toString())
                }
            }
        }
    }

    private fun clearCookies() {
        CookieManager.getInstance().removeAllCookies(null)
    }

    private fun showEmptyView(show: Boolean) {
        with(binding.emptyView) {
            if (show) showEmptyError()
            else hide()
        }
        binding.authWebView.visible(!show)
    }

    override fun onBackPressed() {
        with(binding.authWebView) {
            if (canGoBack()) {
                goBack()
            } else {
                presenter.onBackPressed()
            }
        }
    }

    //region Dagger
    override fun setupComponent(componentKey: String) {
        Injector.getComponent(this, componentKey).inject(this)
    }

    override fun createComponent() =
        Injector.findComponent<AuthFlowComponent>().authComponentFactory().create()
    //endregion
}