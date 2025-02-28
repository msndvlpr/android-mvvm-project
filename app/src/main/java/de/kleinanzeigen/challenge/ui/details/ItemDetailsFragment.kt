package de.kleinanzeigen.challenge.ui.details

import android.animation.ObjectAnimator
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import de.kleinanzeigen.challenge.R
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.AdsDataDetails
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Attribute
import de.kleinanzeigen.challenge.data.repositpry.realestate.model.Document
import de.kleinanzeigen.challenge.databinding.FragmentItemDetailsBinding
import de.kleinanzeigen.challenge.ui.ViewState
import de.kleinanzeigen.challenge.ui.adapter.DetailsInfoRecyclerViewAdapter
import de.kleinanzeigen.challenge.ui.adapter.DocumentsInfoRecyclerViewAdapter
import de.kleinanzeigen.challenge.ui.adapter.FeaturesInfoRecyclerViewAdapter
import de.kleinanzeigen.challenge.ui.adapter.ImageSliderAdapter
import de.kleinanzeigen.challenge.util.fromIsoToLocalizedDateString
import de.kleinanzeigen.challenge.util.toFormattedCurrency
import de.kleinanzeigen.challenge.util.toGermanAddressFormat
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ItemDetailsFragment : Fragment() {

    lateinit var binding: FragmentItemDetailsBinding
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var detailsViewAdapter: DetailsInfoRecyclerViewAdapter
    private lateinit var featuresViewAdapter: FeaturesInfoRecyclerViewAdapter
    private lateinit var documentsViewAdapter: DocumentsInfoRecyclerViewAdapter

    private val viewModel: ItemDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentItemDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageSliderAdapter = ImageSliderAdapter(listOf(), listOf(), {}, {})
        binding.viewPager.apply {
            orientation = ViewPager2.ORIENTATION_HORIZONTAL
            adapter = imageSliderAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.txtSlideNumber.text =
                        String.format("%s / %s", position + 1, imageSliderAdapter.itemCount)
                }
            })
        }

        detailsViewAdapter = DetailsInfoRecyclerViewAdapter(listOf())
        binding.rcvDetailsInfo.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = detailsViewAdapter
        }

        featuresViewAdapter = FeaturesInfoRecyclerViewAdapter(listOf())
        binding.rcvFeaturesInfo.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = featuresViewAdapter
        }

        documentsViewAdapter = DocumentsInfoRecyclerViewAdapter(requireContext(), listOf())
        binding.rcvDocsInfo.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = documentsViewAdapter
        }

        binding.btnRetry.setOnClickListener {
            viewModel.retryLoadData()
        }

    }

    override fun onStart() {
        super.onStart()

        viewModel.onUIEvent(ItemDetailsViewModel.UiEvent.OnInit)
        observeViewModel()
    }

    private fun observeViewModel() {

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.viewState.flowWithLifecycle(lifecycle).collect { state ->

                when (state) {
                    is ViewState.Loading -> {
                        binding.errorContainer.visibility = View.GONE
                        binding.shimmerLayout.shimmerViewContainer.visibility = View.VISIBLE
                        binding.shimmerLayout.shimmerViewContainer.startShimmer()
                        Timber.d("ViewState.Loading")
                    }

                    is ViewState.Failure -> {
                        binding.errorContainer.visibility = View.VISIBLE
                        binding.errorContainer.bringToFront()
                        binding.shimmerLayout.shimmerViewContainer.visibility = View.GONE
                        Timber.d("ViewState.Failure")
                    }

                    is ViewState.Success -> {
                        binding.errorContainer.visibility = View.GONE
                        binding.shimmerLayout.shimmerViewContainer.visibility = View.GONE
                        setupViewPager(
                            state.data.getLowResolutionPictures(),
                            state.data.getHighResolutionPictures(),
                            state.data.title
                        )
                        setupBasicInfoSection(state.data)
                        setupDetailsSection(state.data.attributes)
                        setupFeaturesSection(state.data.features)
                        setupDocumentsSection(state.data.documents)
                        setupDescriptionSection(state.data.description)

                        binding.shimmerLayout.shimmerViewContainer.stopShimmer()
                        binding.shimmerLayout.shimmerViewContainer.visibility = View.GONE
                        Timber.d("ViewState.Success")
                    }

                    else -> TODO()
                }
            }
        }
    }

    private fun setupDescriptionSection(description: String?) {
        if(description.isNullOrEmpty()) {
            binding.dividerDescription.visibility = View.GONE
            binding.txtDescriptionTitle.visibility = View.GONE
            binding.separatorDescription.visibility = View.GONE
            binding.txtDescriptionText.visibility = View.GONE
        } else {
            binding.txtDescriptionTitle.text = getString(R.string.title_description)
            binding.txtDescriptionText.text = description
        }
    }

    private fun setupDocumentsSection(documents: List<Document>?) {
        if(documents.isNullOrEmpty()) {
            binding.dividerDocs.visibility = View.GONE
            binding.txtDocumentsTitle.visibility = View.GONE
            binding.rcvDocsInfo.visibility = View.GONE
        } else {
            binding.txtDocumentsTitle.text = getString(R.string.title_more_information)
            documentsViewAdapter.submitList(documents)
        }
    }

    private fun setupFeaturesSection(features: List<String>?) {
        if(features.isNullOrEmpty()) {
            binding.dividerFeatures.visibility = View.GONE
            binding.txtFeaturesTitle.visibility = View.GONE
            binding.separatorFeatures.visibility = View.GONE
            binding.rcvFeaturesInfo.visibility = View.GONE
        } else {
            binding.txtFeaturesTitle.text = getString(R.string.title_features)
            featuresViewAdapter.submitList(features)
        }
    }

    private fun setupDetailsSection(attributes: List<Attribute>?) {
        if(attributes.isNullOrEmpty()) {
            binding.dividerDetails.visibility = View.GONE
            binding.txtDetailsTitle.visibility = View.GONE
            binding.rcvDetailsInfo.visibility = View.GONE
        } else {
            binding.txtDetailsTitle.text = getString(R.string.title_details)
            detailsViewAdapter.submitList(attributes)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.shimmerLayout.shimmerViewContainer.startShimmer()
    }

    override fun onPause() {
        binding.shimmerLayout.shimmerViewContainer.stopShimmer()
        super.onPause()
    }

    private fun setupBasicInfoSection(data: AdsDataDetails) {
        with(binding) {
            txtTitle.text = data.title
            data.price?.let {
                txtPrice.text = it.toFormattedCurrency()
            }
            data.address?.let {
                txtAddress.text = it.toGermanAddressFormat()
                txtAddress.setOnClickListener { v ->
                    if (it.latitude != null && it.longitude != null) {
                        viewModel.openGoogleMaps(requireContext(), it.latitude!!, it.longitude!!)
                    }
                }
            }
            data.postedDateTime?.let {
                imgCalendar.visibility = View.VISIBLE
                txtDate.text = it.fromIsoToLocalizedDateString()
            }
            data.visits?.let {
                imgVisits.visibility = View.VISIBLE
                txtVisits.text = String.format("$it")
            }
            data.id?.let {
                txtId.text = String.format("ID: %s", it)
            }
        }
    }

    private fun setupViewPager(lowResUrls: List<String>?, highResUrls: List<String>?, title: String?) {
        var bmp: Bitmap? = null

        lowResUrls?.let {
            imageSliderAdapter =
                ImageSliderAdapter(
                    imageUrls = it,
                    highResImageUrls = highResUrls!!,
                    onImageClick = { highResUrl ->
                        showFullSizeImage(highResUrl)

                    }, onBitmapReady = { bitmap ->
                        bmp = bitmap
                    })
            binding.viewPager.adapter = imageSliderAdapter
            binding.txtSlideNumber.visibility = View.VISIBLE
            binding.txtSlideNumber.text = String.format("1 / %s", imageSliderAdapter.itemCount)

            // Setup the sharing operation with loaded Bitmap image
            binding.imgShare.visibility = View.VISIBLE
            binding.imgShare.setOnClickListener { v ->
                bmp?.let { img ->
                    viewModel.shareImageDirectly(requireContext(), img, title ?: "image")
                }
            }
        }

    }

    private fun showFullSizeImage(imageUrl: String) {
        val dialog = Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_full_image)

        val fullImageView: ImageView = dialog.findViewById(R.id.fullSizeImageView)

        Glide.with(this)
            .load(imageUrl)
            .into(fullImageView)

        val window = dialog.window
        window?.setBackgroundDrawable(ColorDrawable(Color.BLACK))

        val closeButton: ImageView = dialog.findViewById(R.id.closeButton)
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }


}