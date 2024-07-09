package com.gdalamin.bcs_pro.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.gdalamin.bcs_pro.databinding.FragmentMoreBinding
import com.gdalamin.bcs_pro.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoreFragment : BaseFragment<FragmentMoreBinding>(FragmentMoreBinding::inflate) {

    override fun loadUi() {
        listener()
    }

    private fun listener() = binding.apply {
        messengerChat.setOnClickListener { openMessenger() }
        facebookGroup.setOnClickListener {
            val pageId = "100094890072982" //   Facebook page ID
            val pageName = "BCS PRO" //  name of the Messenger group
            val intent = Intent(
                Intent.ACTION_VIEW, Uri.parse(
                    "fb://profile/$pageId"
                )
            )
            intent.putExtra("title", pageName)
            try {
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    context,
                    "Please install Facebook Messenger",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        appRating.setOnClickListener {

        }
        shareBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            val playStoreLink = "https://play.google.com/store/apps/details?id=com.gdalamin.bcs_pro"
            shareIntent.putExtra(Intent.EXTRA_TEXT, playStoreLink)
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
        aboutUs.setOnClickListener {
            val pixatoneUrl = "http://pixatone.com/"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(pixatoneUrl))
            startActivity(intent)
        }
        privacy.setOnClickListener {
            val privacyPolicyUrl =
                "https://doc-hosting.flycricket.io/bcs-pro-privacy-policy/5c7da9fa-9a36-49af-915c-27bb7a5483df/privacy"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(privacyPolicyUrl))
            startActivity(intent)
        }
        termsAndConditions.setOnClickListener {
            val termsConditionUrl =
                "https://doc-hosting.flycricket.io/bcs-pro-terms-of-use/4267f876-2eee-43ce-8417-0fd41c538b03/terms"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(termsConditionUrl))
            startActivity(intent)
        }
    }


    private fun openMessenger() {
        val userIdOrGroupId = "100094890072982"
        // Launch Messenger chat with the specified user or group ID
        val intent = Intent(
            Intent.ACTION_VIEW, Uri.parse(
                "fb-messenger://user/$userIdOrGroupId"
            )
        )
        intent.putExtra(Intent.EXTRA_TEXT, "Hello, let's chat!") // Optional: add a message
        intent.setPackage("com.facebook.orca") // Set the package name of Messenger app
        try {
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            // Messenger is not installed, handle the error here
        }
    }
}