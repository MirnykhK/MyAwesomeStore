package ru.adminmk.myawesomestore.view

import android.os.Bundle
import android.view.View
import ru.adminmk.myawesomestore.databinding.ActivityMainBinding
import android.os.Build
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.android.support.DaggerAppCompatActivity
import ru.adminmk.myawesomestore.R
import ru.adminmk.myawesomestore.viewmodel.host.HostViewModel

class MainActivity : DaggerAppCompatActivity(), HostContract {

    private lateinit var binding: ActivityMainBinding

    val hostViewModel by viewModels<HostViewModel> {
        ViewModelProvider.NewInstanceFactory()
    }

    private var navBarHeight: Int? = null
    private var statusBarHeight: Int? = null

    private var isSavedStateLaunch = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isSavedStateLaunch = savedInstanceState != null

        if (!isSavedStateLaunch) {
            BottomSheetBehavior.from(binding.bottomContainer.frame).apply {
                peekHeight = 0
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        onOnApplyWindowInsets()
        expandView()

        if (!isSavedStateLaunch) {
            setupHostStatusBarTheme(true)
        }
        setupBottomToolbarBttns()
    }

    private fun onOnApplyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->

            insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom.apply {
                navBarHeight = this

                (binding.bottomStub.layoutParams as CoordinatorLayout.LayoutParams).height = this
            }

            insets.getInsets(WindowInsetsCompat.Type.statusBars()).top.let {
                hostViewModel.initStatusBarHeight(it)
                statusBarHeight = it
            }

            if (isSavedStateLaunch) {
                setTranslucentNavBar()
                hideBottomStubAndRevealBottomToolbar(isSavedStateLaunch)
                binding.logoView.isVisible = false
            }

            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupBottomToolbarBttns() {
        binding.bottomContainer.bttnHome.setupNavigation(R.id.mainFragment)
        binding.bottomContainer.bttnShop.setupNavigation(R.id.categoriesFragment)

        with(binding.fragmentContainer.id) {
            (supportFragmentManager.findFragmentById(this) as NavHostFragment).apply {
                this.navController.addOnDestinationChangedListener { _,
                    destination: NavDestination,
                    _ ->
                    when (destination.id) {
                        R.id.mainFragment -> binding.bottomContainer.bttnHome.isChecked = true
                        R.id.categoriesFragment -> binding.bottomContainer.bttnShop.isChecked = true
                    }
                }
            }
        }
    }

    private fun View.setupNavigation(fragmentDestinationId: Int) {
        this.setOnClickListener { hostViewModel.navigate(fragmentDestinationId) }
    }

//    override fun onResume() {
//        super.onResume()
//
//        if (!isSavedStateLaunch && !hostViewModel.isAppLaunched) {
//            hostViewModel.loadRemoteData()
//        }
//    }

    private fun hideBottomStubAndRevealBottomToolbar(isSavedStateLaunch: Boolean) {
        binding.bottomStub.isVisible = false

        navBarHeight?.let {
            (binding.root.layoutParams as FrameLayout.LayoutParams)
                .bottomMargin = it
        }

        BottomSheetBehavior.from(binding.bottomContainer.frame).apply {
            peekHeight = resources.getDimensionPixelSize(R.dimen.bottom_toolbar_peek_height)

            if (!isSavedStateLaunch) {
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun expandView() {
        this.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                this.setDecorFitsSystemWindows(false)
            } else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            }

            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    private fun setTranslucentNavBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window?.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window?.decorView?.let {
                it.systemUiVisibility =
                    it.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    override fun setupTranslucentHostStatusBarTheme() {
        this.setupHostStatusBarTheme(true)
        window?.statusBarColor = ContextCompat.getColor(this, R.color.background)
    }

    override fun setupTransparentHostStatusBarTheme() {
        window?.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
    }

    override fun collapseHostToBorders() {
        this.window?.apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                this.setDecorFitsSystemWindows(true)
            } else {
                @Suppress("DEPRECATION")
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION.inv()
            }
        }
    }

    override fun expandHost() {
        expandView()
    }

    override fun obtainHostViewModel() = hostViewModel

    override fun setupHostStatusBarTheme(isLight: Boolean) {
        setupStatusBarTheme(isLight)
    }

    override fun collapseBottomToolbar() {
        BottomSheetBehavior.from(binding.bottomContainer.frame).state =
            BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setupStatusBarTheme(isLight: Boolean) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (isLight) {
                window?.insetsController?.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            } else {
                window?.insetsController?.setSystemBarsAppearance(
                    0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                )
            }
        } else {
            @Suppress("DEPRECATION")
            window?.decorView?.let {
                it.systemUiVisibility = if (isLight) {
                    it.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    it.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
            }
        }
    }

    override fun obtainStatusBarHeight() = statusBarHeight

    override fun performAnimation() {
        binding.logoView.performAnimation {
            hostViewModel.isAppLaunched = true
            setupHostStatusBarTheme(false)
            setTranslucentNavBar()
            hideBottomStubAndRevealBottomToolbar(isSavedStateLaunch)
        }
    }
}

interface HostContract {
    fun setupTranslucentHostStatusBarTheme()
    fun setupTransparentHostStatusBarTheme()
    fun collapseHostToBorders()
    fun expandHost()
    fun obtainHostViewModel(): HostViewModel
    fun setupHostStatusBarTheme(isLight: Boolean)
    fun collapseBottomToolbar()
    fun obtainStatusBarHeight(): Int?
    fun performAnimation()
}
