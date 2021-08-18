package ru.adminmk.myawesomestore.view.categories

import android.animation.LayoutTransition
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.adminmk.myawesomestore.R
import ru.adminmk.myawesomestore.databinding.FragmentCategoriesBinding
import ru.adminmk.myawesomestore.model.SaleCategorie
import ru.adminmk.myawesomestore.model.TopCategorie
import ru.adminmk.myawesomestore.view.HostContract
import ru.adminmk.myawesomestore.view.ParentFragment
import ru.adminmk.myawesomestore.view.categories.adapter_delegates.ShimmerStub
import ru.adminmk.myawesomestore.view.categories.adapter_delegates.categorieDelegate
import ru.adminmk.myawesomestore.view.categories.adapter_delegates.categorieShimmerDelegate
import ru.adminmk.myawesomestore.view.custom_view.FontSpan
import ru.adminmk.myawesomestore.viewmodel.EventObserver
import ru.adminmk.myawesomestore.viewmodel.categories.CategoriesLoaderViewModel
import timber.log.Timber
import javax.inject.Inject

private const val TOP_CATEGORIE_ACTION_DELAY = 200L

class CategoriesFragment : ParentFragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding: FragmentCategoriesBinding
        get() = _binding!!

    private val categoriesAdapter by lazy {
        ListDelegationAdapter(categorieDelegate {
            Timber.tag("CategoriesFragment").d("click!")
        })
    }
    private val categoriesShimmerAdapter by lazy { ListDelegationAdapter(categorieShimmerDelegate()) }

    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory

    override val localViewModel by viewModels<CategoriesLoaderViewModel> {
        viewModelProviderFactory
    }

    private var isSavedStateLaunch = false

    private val activityLifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
        fun onAny(owner: LifecycleOwner) {
            if (owner.lifecycle.currentState == Lifecycle.State.CREATED) {
                Timber.tag("MainActivity").d("on activity created")
                activity?.let {
                    (it as HostContract).setupHostStatusBarTheme(true)
                    it.lifecycle.removeObserver(this)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isSavedStateLaunch = savedInstanceState != null

        initTopCategoriesBttns()
        initCategoriesRecycler()
        initToolbar()
        initShimmer()
        initMainData()

        setupTopMargin()
    }

    private fun initToolbar() {
        { findNavController().popBackStack() }.apply {
            binding.toolbar.setNavigationOnClickListener { invoke() }
            binding.categoriesShimmer.toolbar.setNavigationOnClickListener { invoke() }
        }
    }

    private fun initMainData() {
        localViewModel.isDataAvailable.observe(
            viewLifecycleOwner,
            Observer
            { isDataAvailable ->
                binding.categoriesRoot.isVisible = isDataAvailable
                localViewModel.remoteData.value?.let {
                    initSaleBttn(it.saleCategorie)
                    categoriesAdapter.items = it.mainCategories
                }
            }
        )
    }

    private fun initShimmer() {

        binding.categoriesShimmer.categoriesRecyclerShimmer.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        categoriesShimmerAdapter.items = (1..4).map { ShimmerStub() }

        binding.categoriesShimmer.categoriesRecyclerShimmer.adapter = categoriesShimmerAdapter

        localViewModel.dataLoading.observe(
            viewLifecycleOwner,
            Observer
            { isLoading ->
                with(binding.categoriesShimmer) {
                    categoriesRoot.isVisible = isLoading
                    if (isLoading) {
                        shimmerContainer.startShimmer()
                    } else {
                        shimmerContainer.stopShimmer()
                    }
                }
            }
        )
    }

    private fun initCategoriesRecycler() {
        binding.categoriesRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.categoriesRecycler.adapter = categoriesAdapter
        binding.categoriesRecycler.edgeEffectFactory = RecyclerViewEdgeEffectFactory()
        binding.categoriesRecycler.setOnScrollChangeListener { _, _, _, _, oldScrollY ->
            collapseBottomToolbarOnFirstScroll(oldScrollY)
        }
    }

    private fun initTopCategoriesBttns() {
        selectTopCategorie(null)
        binding.topCategoriesHolder.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        binding.womanCategorie.setOnClickListener {
            selectTopCategorie(TopCategorie.WOMEN)
        }
        binding.manCategorie.setOnClickListener {
            selectTopCategorie(TopCategorie.MEN)
        }
        binding.kidsCategorie.setOnClickListener {
            selectTopCategorie(TopCategorie.KIDS)
        }
    }

    private fun selectTopCategorie(newTopCategorie: TopCategorie?) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(TOP_CATEGORIE_ACTION_DELAY)

            var selectedTopCategorie = localViewModel.getTopCategory()

            if (newTopCategorie == selectedTopCategorie) return@launch

            if (newTopCategorie != null) {
                selectedTopCategorie = newTopCategorie
                localViewModel.saveTopCategory(newTopCategorie)
            }

            val defaultTypeface = Typeface.create(
                ResourcesCompat.getFont(requireContext(), R.font.metropolis_regular),
                Typeface.NORMAL
            )

            binding.womanCategorie.typeface = defaultTypeface
            binding.manCategorie.typeface = defaultTypeface
            binding.kidsCategorie.typeface = defaultTypeface

            val selectionTypeface = Typeface.create(
                ResourcesCompat.getFont(requireContext(), R.font.metropolis_semi_bold),
                Typeface.NORMAL
            )

            val params = binding.selectPlank.layoutParams as ConstraintLayout.LayoutParams
            when (selectedTopCategorie) {
                TopCategorie.WOMEN -> {
                    binding.womanCategorie.typeface = selectionTypeface
                    params.startToStart = R.id.womanCategorie
                    params.endToEnd = R.id.womanCategorie
                }
                TopCategorie.MEN -> {
                    binding.manCategorie.typeface = selectionTypeface
                    params.startToStart = R.id.manCategorie
                    params.endToEnd = R.id.manCategorie
                }
                TopCategorie.KIDS -> {
                    binding.kidsCategorie.typeface = selectionTypeface
                    params.startToStart = R.id.kidsCategorie
                    params.endToEnd = R.id.kidsCategorie
                }
            }
            binding.selectPlank.layoutParams = params
        }
    }

    override fun selectBottomToolbarNavCommand(fragmentDestinationId: Int): NavDirections? {
        return when (fragmentDestinationId) {
            R.id.mainFragment -> CategoriesFragmentDirections.actionCategoriesFragmentToMainFragment()
            else -> null
        }
    }

    private fun initSaleBttn(saleCategorie: SaleCategorie?) {
        if (saleCategorie == null) {
            binding.bttnSale.isVisible = false
            return
        }

        binding.bttnSale.isVisible = true

        val spannableStringBuilder = SpannableStringBuilder(saleCategorie.header)
        spannableStringBuilder.append()

        val typefaceSemiBold = Typeface.create(
            ResourcesCompat.getFont(requireContext(), R.font.metropolis_semi_bold),
            Typeface.NORMAL
        )
        val headerTextSize = resources.getDimensionPixelSize(R.dimen.text_size_24)

        spannableStringBuilder.setSpan(
            FontSpan(typefaceSemiBold),
            0,
            spannableStringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            AbsoluteSizeSpan(headerTextSize),
            0,
            spannableStringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        spannableStringBuilder.append("\n")

        spannableStringBuilder.append(saleCategorie.content)
        val typefaceMedium = Typeface.create(
            ResourcesCompat.getFont(requireContext(), R.font.metropolis_medium),
            Typeface.NORMAL
        )
        val contentTextSize = resources.getDimensionPixelSize(R.dimen.text_size_14)

        spannableStringBuilder.setSpan(
            FontSpan(typefaceMedium),
            spannableStringBuilder.length - saleCategorie.content.length,
            spannableStringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableStringBuilder.setSpan(
            AbsoluteSizeSpan(contentTextSize),
            spannableStringBuilder.length - saleCategorie.content.length,
            spannableStringBuilder.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.bttnSale.text = spannableStringBuilder
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is FragmentActivity) {
            context.lifecycle.addObserver(activityLifecycleObserver)
        }
    }

    override fun initFragmentOnAttachToHost(context: HostContract) {
        super.initFragmentOnAttachToHost(context)

        try {
            context.setupHostStatusBarTheme(true)
        } catch (e: Exception) {
            // may fall down on restore state after termination
        }
    }

    private fun setupTopMargin() {
        if (isSavedStateLaunch) {
            localViewModel.statusBarHeightMediatorLiveData.observe(
                viewLifecycleOwner,
                EventObserver {
                    (binding.root.layoutParams as FrameLayout.LayoutParams).topMargin = it
                }
            )
        } else {
            activity?.let { fragmentActivity ->
                (fragmentActivity as HostContract).apply {
                    obtainStatusBarHeight()?.let {
                        (binding.root.layoutParams as FrameLayout.LayoutParams).topMargin = it
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        localViewModel.loadRemoteData()
    }

    override fun onPause() {
        super.onPause()

        localViewModel.cancelJob()
    }
}
