package com.example.inventory.appFragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inventory.R
import com.example.inventory.databinding.FragmentCatalogBinding
import com.example.inventory.utils.ProductAdapter
import com.example.inventory.utils.ProductData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CatalogFragment : Fragment(), AddFragment.DialogNextBtnClickListener,
    ProductAdapter.ProductAdapterClicksInterface {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var binding: FragmentCatalogBinding
    private var popUpFragment: AddFragment? = null
    private lateinit var adapter: ProductAdapter
    private lateinit var mList:MutableList<ProductData>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()

    }

    private fun registerEvents() {
        binding.addBtn.setOnClickListener {
            if (popUpFragment != null)
                childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
            popUpFragment = AddFragment()
            popUpFragment!!.setListener(this)
            popUpFragment!!.show(
                childFragmentManager,
                "AddFragment"
            )
        }


    }

    private fun init(view: View) {
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
            .child("Products").child(auth.currentUser?.uid.toString())

        binding.catalogRv.setHasFixedSize(true)
        binding.catalogRv.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter = ProductAdapter(mList)
        adapter.setListener(this)
        binding.catalogRv.adapter = adapter

    }

    private fun getDataFromFirebase(){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for (productSnapshot in snapshot.children) {

                    val productName = productSnapshot.child("productName").getValue(String::class.java)
                    val productDescription = productSnapshot.child("productDescription").getValue(String::class.java)
                    val costPrice = productSnapshot.child("costPrice").getValue(String::class.java)
                    val sellingPrice = productSnapshot.child("sellingPrice").getValue(String::class.java)
                    val quantity = productSnapshot.child("quantity").getValue(String::class.java)

                    val productData = ProductData(
                        productName,
                        productDescription,
                        costPrice,
                        sellingPrice,
                        quantity
                    )
                    mList.add(productData)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveProduct(
        product: String,
        etProductName: TextInputEditText,
        productDesc: String,
        etProductDescription: TextInputEditText,
        productCost: String,
        etCostPrice: TextInputEditText,
        productSell: String,
        etSellingPrice: TextInputEditText,
        productQuan: String,
        etQuantity: TextInputEditText
    ) {
        val productName = etProductName.text.toString()
        val productDescription = etProductDescription.text.toString()
        val costPrice = etCostPrice.text.toString()
        val sellingPrice = etSellingPrice.text.toString()
        val quantity = etQuantity.text.toString()

        val productData = ProductData(
            productName,
            productDescription,
            costPrice,
            sellingPrice,
            quantity
        )
        databaseReference.push().setValue(productData).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Product Added Successfully", Toast.LENGTH_SHORT).show()
                etProductName.text = null
                etProductDescription.text = null
                etCostPrice.text = null
                etSellingPrice.text = null
                etQuantity.text = null

            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            //popUpFragment.dismiss()
        }
    }

    override fun onUpdateProduct(
        product: String,
        etProductName: TextInputEditText,
        productDesc: String,
        etProductDescription: TextInputEditText,
        productCost: String,
        etCostPrice: TextInputEditText,
        productSell: String,
        etSellingPrice: TextInputEditText,
        productQuan: String,
        etQuantity: TextInputEditText
    ) {
        TODO("Not yet implemented")
    }

    override fun onDeleteProductBtnClicked(productData: ProductData) {
        deleteProduct(productData)
    }
    private fun deleteProduct(productData: ProductData) {
        val productName = productData.productName
        if (productName != null) {
            val productRef = FirebaseDatabase.getInstance().getReference("Products").child(productName)
            productRef.removeValue()
                .addOnSuccessListener {
                    Log.d(TAG, "Product deleted successfully")
                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to delete product", exception)
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                }
        }else{
            Log.e(TAG, "Product name is null")
            Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onReadProductBtnClicked(productData: ProductData) {
        viewProduct(productData)
    }

    private fun viewProduct(productData: ProductData) {
        val viewFragment =ViewFragment.newInstance(productData)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.eachCatalog,viewFragment)
            .addToBackStack(null)
            .commit()
    }


//    override fun onDeleteProductBtnClicked(productData: ProductData) {
//        productData.productName?.let { it ->
//            databaseReference.child(it).removeValue().addOnCompleteListener {
//                if (it.isSuccessful) {
//                    Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
//                }else {
//                    Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
//
//
//    override fun onEditProductBtnClicked(productData: ProductData) {
//        if (popUpFragment != null)
//            childFragmentManager.beginTransaction().remove(popUpFragment!!).commit()
//
//        popUpFragment!!.setListener(this)
//        //popUpFragment!!.show(childFragmentManager, AddFragment.TAG)
//    }
//
//    override fun onReadProductBtnClicked(productData: ProductData) {
////        popUpReadFragment = ReadFragment()
////        popUpReadFragment.setLis
//
//    }


}