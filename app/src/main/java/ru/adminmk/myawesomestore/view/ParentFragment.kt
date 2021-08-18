package ru.adminmk.myawesomestore.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFade
import com.google.android.material.transition.MaterialSharedAxis
import dagger.android.support.DaggerFragment
import ru.adminmk.myawesomestore.viewmodel.EventObserver
import ru.adminmk.myawesomestore.viewmodel.FragmentViewModel

abstract class ParentFragment : DaggerFragment() {
    protected var isFirstBottomToolbarScrollTookPlace = false

    protected abstract val localViewModel: FragmentViewModel<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            reenterTransition = this
            enterTransition = this
        }

        MaterialFade().apply {
            exitTransition = this
            returnTransition = this
        }
    }

    override fun onDetach() {
        super.onDetach()

        activity?.let {
            (it as HostContract).apply {
                obtainHostViewModel().apply {
                    localViewModel.navigationStateMediatorLiveData.removeSource(navigationState)

                    localViewModel.statusBarHeightMediatorLiveData.removeSource(statusBarHeight)
                }
            }
        }
    }

    protected fun collapseBottomToolbarOnFirstScroll(oldScrollY: Int) {
        if (!isFirstBottomToolbarScrollTookPlace && oldScrollY != 0) {
            collapseBottomToolbar()
            isFirstBottomToolbarScrollTookPlace = true
        }
    }

    private fun collapseBottomToolbar() {
        activity?.let {
            (it as HostContract).collapseBottomToolbar()
        }
    }

    open fun doOnPreAttach(context: Context) {}

    override fun onAttach(context: Context) {
        super.onAttach(context)

        doOnPreAttach(context)

        if (context is HostContract) {
            with(context) {
                localViewModel.navigationStateMediatorLiveData.addSource(
                    obtainHostViewModel().navigationState
                ) {
                    localViewModel.navigationStateMediatorLiveData.value = it
                }

                localViewModel.statusBarHeightMediatorLiveData.addSource(
                    obtainHostViewModel().statusBarHeight
                ) {
                    localViewModel.statusBarHeightMediatorLiveData.value = it
                }

                initFragmentOnAttachToHost(context)
            }
        }
    }

    protected open fun initFragmentOnAttachToHost(context: HostContract) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBottomToolbar()
    }

    private fun setupBottomToolbar() {
        localViewModel.navigationStateMediatorLiveData.observe(
            viewLifecycleOwner,
            EventObserver {
                viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                    selectBottomToolbarNavCommand(it)?.also {
                        findNavController().navigate(it)
                    }
                }
            }
        )
    }

    protected abstract fun selectBottomToolbarNavCommand(fragmentDestinationId: Int): NavDirections?
}
