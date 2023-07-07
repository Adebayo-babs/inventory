package com.example.inventory.appFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.inventory.R
import com.example.inventory.databinding.FragmentAddBinding
import com.example.inventory.utils.ProductData
import com.google.android.material.textfield.TextInputEditText
import java.lang.Double.isNaN


class AddFragment : DialogFragment() {

    private lateinit var binding: FragmentAddBinding
    private lateinit var listener : DialogNextBtnClickListener
    private var productData: ProductData? = null

    fun setListener(listener : DialogNextBtnClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments != null){
            productData = ProductData(arguments?.getString("productName").toString(), arguments?.getString("productName"),
                arguments?.getString("productDescription").toString(), arguments?.getString("productDescription"),

                )
        }
        registerEvents()
    }

    private fun registerEvents() {
        binding.addProduct.setOnClickListener {
            val nProductName = binding.etProductName.text.toString()
            val nProductDescription = binding.etProductDescription.text.toString()
            val nCostPrice = binding.etCostPrice.text.toString()
            val nSellingPrice = binding.etSellingPrice.text.toString()
            val nQuantity = binding.etQuantity.text.toString()

            if (nProductName.isNotEmpty() && nProductDescription.isNotEmpty() && nCostPrice.isNotEmpty() && nSellingPrice.isNotEmpty() && nQuantity.isNotEmpty()) {

                if (productData == null) {
                    listener.onSaveProduct(nProductName, binding.etProductName,
                        nProductDescription, binding.etProductDescription,
                        nCostPrice, binding.etCostPrice,
                        nSellingPrice, binding.etSellingPrice,
                        nQuantity, binding.etQuantity)
                }else{
                    productData?.productName = nProductName
                    productData?.productDescription = nProductDescription
                    productData?.costPrice = nCostPrice
                    productData?.sellingPrice = nSellingPrice
                    productData?.quantity = nQuantity

                    listener.onUpdateProduct(nProductName, binding.etProductName,
                        nProductDescription, binding.etProductDescription,
                        nCostPrice, binding.etCostPrice,
                        nSellingPrice, binding.etSellingPrice,
                        nQuantity, binding.etQuantity)
                }


            } else {
                Toast.makeText(context, "Fill in empty fields", Toast.LENGTH_SHORT).show()
            }
        }
        binding.calculateEquity.setOnClickListener {

            val productName = binding.etProductName.text.toString()
            val costPrice = binding.etCostPrice.text.toString().toFloatOrNull()
            val sellingPrice = binding.etSellingPrice.text.toString().toFloatOrNull()

            if (costPrice != null && sellingPrice != null && productName.isNotEmpty()) {
                val result = (sellingPrice?.times(100)?.div(costPrice!!))?.minus(100)
                val finalResult = String.format("%.2f", result)
                binding.resultText.text = "You are making $finalResult% profit per ${productName} sold"
            }else{
                Toast.makeText(context, "Fill in empty fields with valid inputs", Toast.LENGTH_SHORT).show()
            }
        }
    }

    interface DialogNextBtnClickListener{
        fun onSaveProduct(product: String,
                          etProductName: TextInputEditText,
                          productDesc: String,
                          etProductDescription : TextInputEditText,
                          productCost: String,
                          etCostPrice : TextInputEditText,
                          productSell: String,
                          etSellingPrice : TextInputEditText,
                          productQuan: String,
                          etQuantity : TextInputEditText
        )
        fun onUpdateProduct(product: String,
                          etProductName: TextInputEditText,
                          productDesc: String,
                          etProductDescription : TextInputEditText,
                          productCost: String,
                          etCostPrice : TextInputEditText,
                          productSell: String,
                          etSellingPrice : TextInputEditText,
                          productQuan: String,
                          etQuantity : TextInputEditText
        )
    }
}