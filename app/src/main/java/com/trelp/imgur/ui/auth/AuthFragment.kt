package com.trelp.imgur.ui.auth

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.trelp.imgur.R
import com.trelp.imgur.databinding.FragmentAuthBinding
import com.trelp.imgur.di.Injector
import com.trelp.imgur.di.auth.AuthComponent
import com.trelp.imgur.di.flow.auth.AuthFlowComponent
import com.trelp.imgur.presentation.auth.AuthPresenter
import com.trelp.imgur.presentation.auth.AuthView
import com.trelp.imgur.ui.base.BaseFragment
import moxy.ktx.moxyPresenter
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class AuthFragment : BaseFragment<AuthComponent>(R.layout.fragment_auth), AuthView {

    private var fragmentAuthBinding: FragmentAuthBinding? = null
    private val binding get() = fragmentAuthBinding!!

    @Inject
    lateinit var presenterProvider: Provider<AuthPresenter>

    private val presenter by moxyPresenter { presenterProvider.get() }

    //region LifeCycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.d(javaClass.simpleName)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentAuthBinding = FragmentAuthBinding.bind(view)

        binding.toolbar.setNavigationOnClickListener { presenter.onBackPressed() }

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
        fragmentAuthBinding = null

        super.onDestroyView()
    }
    //endregion

    //region AuthView
    override fun loadUrl(url: String) {
        binding.authWebView.loadUrl(url)
    }

    override fun showProgress() {
        Toast.makeText(requireContext(), "Show progress", Toast.LENGTH_SHORT).show()
    }

    override fun hideProgress() {
        Toast.makeText(requireContext(), "Hide progress", Toast.LENGTH_SHORT).show()
    }

    override fun showError(error: Throwable) {
        Toast.makeText(requireContext(), "Show error", Toast.LENGTH_SHORT).show()
    }

    override fun hideError() {
        Toast.makeText(requireContext(), "Hide error", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configWebView() {
        with(binding.authWebView) {
            settings.javaScriptEnabled = true
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    hideProgress()
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    showProgress()
                    super.onPageStarted(view, url, favicon)
                }

                override fun shouldOverrideUrlLoading(view: WebView, url: String) =
                    presenter.onRedirect(url)

                @RequiresApi(value = Build.VERSION_CODES.N)
                override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest) =
                    presenter.onRedirect(request.url.toString())
            }
        }
    }

    private fun clearCookies() {
        CookieManager.getInstance().removeAllCookies(null)
    }
    //endregion

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
    override fun setupComponent() {
        Injector.getComponent(this).inject(this)
    }

    override fun getComponentKey() = "Auth"

    override fun createComponent() =
        Injector.findComponent<AuthFlowComponent>().authComponentFactory().create()
    //endregion
}