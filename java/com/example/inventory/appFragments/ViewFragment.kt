package com.example.inventory.appFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.inventory.R
import com.example.inventory.databinding.FragmentViewBinding
import com.example.inventory.utils.ProductData
import com.google.gson.Gson


class ViewFragment : Fragment() {

    private lateinit var binding: FragmentViewBinding

    companion object {
        private const val ARG_PRODUCT_DATA = "productData"
        fun newInstance(productData: ProductData): ViewFragment {
            val fragment = ViewFragment()
            val args = Bundle()
            val productDataJson = Gson().toJson(productData)
            args.putString(ARG_PRODUCT_DATA, productDataJson)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        if (args != null && args.containsKey(ARG_PRODUCT_DATA)) {
            val productDataJson = args.getString(ARG_PRODUCT_DATA)
            val productData = Gson().fromJson(productDataJson, ProductData::class.java)

            binding.viewPageProductName.text = productData.productName
            binding.viewPageDescription.text = productData.productDescription
            binding.viewPageCP.text = productData.costPrice
            binding.viewPageSP.text = productData.sellingPrice
            binding.viewPageQty.text = productData.quantity
        }
    }


}