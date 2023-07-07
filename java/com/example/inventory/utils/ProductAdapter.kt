package com.example.inventory.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.inventory.databinding.FragmentEachCatalogBinding

class ProductAdapter( private val list: MutableList<ProductData>) :
        RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    private var listener: ProductAdapterClicksInterface? = null
    fun setListener(listener:ProductAdapterClicksInterface) {
        this.listener = listener
    }
    inner class ProductViewHolder(val binding: FragmentEachCatalogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = FragmentEachCatalogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        with(holder){
            with(list[position]){
                binding.productNameTv.text = this.productName
                binding.quantityTv.text = this.quantity
                binding.productPriceTv.text = this.sellingPrice

                binding.eachCatalog.setOnClickListener {
                    listener?.onReadProductBtnClicked(this)
                }


                binding.deleteBtn.setOnClickListener {
                    listener?.onDeleteProductBtnClicked(this)
                }

//                binding.editBtn.setOnClickListener {
//                    listener?.onEditProductBtnClicked(this)
//                }

//                binding.readBtn.setOnClickListener {
//                    listener?.onReadProductBtnClicked(this)
//                }

            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface ProductAdapterClicksInterface{
        fun onDeleteProductBtnClicked(productData: ProductData)
//        fun onEditProductBtnClicked(productData: ProductData)
//
        fun onReadProductBtnClicked(productData: ProductData)
    }
}