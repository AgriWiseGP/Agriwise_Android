package com.example.agriwise.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.agriwise.R
import com.example.agriwise.data.model.FeaturesData
import com.example.agriwise.databinding.FragmentHomeBinding
import com.example.agriwise.ui.adapter.FeaturesAdapter

class HomeFragment : Fragment() {

private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.
  private val binding get() = _binding!!

  @SuppressLint("SuspiciousIndentation")
  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root
      val adapter = FeaturesAdapter()
    binding.homeRV.adapter = adapter
        adapter.submitList(listOf(FeaturesData(R.drawable.camera__1_,getString(R.string.my_crop_safety),getString(R.string.crop_safety_content)),
            FeaturesData(R.drawable.sapling__3_,getString(R.string.crops_we_recommend),getString(R.string.recommending_crops)),
            FeaturesData(R.drawable.plant__1_,getString(R.string.Soil_suitability_for_cultivation),getString(R.string.soil_suitability_content)),
            FeaturesData(R.drawable.pest__1_,getString(R.string.my_crop_future),getString(R.string.crop_future_content)),
            FeaturesData(R.drawable.camera__1_,getString(R.string.soil_classification),getString(R.string.recommending_crops)),
            FeaturesData(R.drawable.greenhouse__4_,getString(R.string.nearest_plantation),getString(R.string.recommending_crops)),
            FeaturesData(R.drawable.laboratory__1_,getString(R.string.nearest_laboratory),getString(R.string.recommending_crops)),
            FeaturesData(R.drawable.fertilizer__3_,getString(R.string.purchase_of_fertilizers_and_treatment),getString(R.string.recommending_crops))

        
            ))
      adapter.notifyDataSetChanged()
    return root
  }

override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}