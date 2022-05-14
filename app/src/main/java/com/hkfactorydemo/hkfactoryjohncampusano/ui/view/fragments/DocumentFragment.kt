package com.hkfactorydemo.hkfactoryjohncampusano.ui.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.hkfactorydemo.hkfactoryjohncampusano.R
import com.hkfactorydemo.hkfactoryjohncampusano.databinding.FragmentDocumentBinding
import com.hkfactorydemo.hkfactoryjohncampusano.domain.model.Purchase
import com.hkfactorydemo.hkfactoryjohncampusano.ui.viewModels.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentFragment : Fragment(){

    private lateinit var binding: FragmentDocumentBinding
    private var subtotal = 0
    private var total = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        container?.removeAllViews()

        binding = FragmentDocumentBinding.inflate(inflater, container, false)

        val purchaseViewModel: PurchaseViewModel by viewModels()

        binding.countEt.text = purchaseViewModel.count.toString()



        binding.sellerName.text = arguments?.getString("sellerName")
        binding.ncf.text = arguments?.getString("ncfNumber")
        binding.customerName.text = arguments?.getString("customerName")
        binding.vatId.text = arguments?.getString("customerVatId")
        purchaseViewModel.purchaseModel.observe(viewLifecycleOwner){_->



            //purchaseViewModel.createPurchase(purchase)

        }
        binding.addToCart.setOnClickListener {


            subtotal =  binding.productPrice.text.toString().toInt() * purchaseViewModel.count

            binding.subtotal.text = subtotal.toString()

           /* purchase.seller = binding.sellerName.text.toString()
            purchase.ncf = binding.ncf.text.toString()
            purchase.customerName = binding.customerNameField.text.toString()
            purchase.vatId = binding.customerVatIdField.text.toString()
            purchase.productCode = binding.codePurchaseEt.text.toString()
            purchase.productName = binding.productNameEt.text.toString()
            purchase.productPrice = binding.productPrice.toString().toInt()
            purchase.productQuantity = purchaseViewModel.count
            purchase.subtotal = subtotal*/

        }



        binding.minusBtn.setOnClickListener {
            purchaseViewModel.minusNumber()
            binding.countEt.text = purchaseViewModel.count.toString()
        }

        binding.plusBtn.setOnClickListener {
             purchaseViewModel.addNumber()
            binding.countEt.text = purchaseViewModel.count.toString()
        }

        binding.btnBackDispatch.setOnClickListener{
            val customerFragment = CustomerFragment()
            parentFragmentManager.commit {
                setCustomAnimations(
                    androidx.appcompat.R.anim.abc_slide_in_bottom,
                    com.google.android.material.R.anim.abc_fade_out,
                )
                replace(R.id.documentFragment, customerFragment)
            }
        }
        return binding.root
    }



}