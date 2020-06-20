package com.mkitsimple.counterboredom.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.User
import com.mkitsimple.counterboredom.ui.auth.RegisterActivity
import com.mkitsimple.counterboredom.util.longToast
import com.mkitsimple.counterboredom.util.toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    companion object {
        fun newInstance() = ProfileFragment()
        var currentUser: User? = null
    }

    private lateinit var viewModel: ProfileViewModel
    private var profileChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        //buttonSaveChanges.isEnabled = false
        if(MainActivity.currentUser?.profileImageUrl != "null") {
            Picasso.get().load(MainActivity.currentUser?.profileImageUrl)
                .into(circleImageViewProfile)
        }
        editTextProfile.setText(MainActivity.currentUser!!.username)

        circleImageViewProfile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        buttonSaveChanges.setOnClickListener {
            uploadImageToFirebaseStorage()
        }

        profileBackArrow.setOnClickListener {
            finish()
        }
    }

    var selectedPhotoUri: Uri? = null
    var mUploadedImageUri: String? = null
    var bitmap : Bitmap? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            // proceed and check what the selected image was....
            Log.d(RegisterActivity.TAG, "Photo was selected")

            selectedPhotoUri = data.data
            bitmap = MediaStore.Images.Media
                .getBitmap(this.contentResolver, selectedPhotoUri)

            circleImageViewProfile.setImageBitmap(bitmap)
            //buttonSelectPhotoProfile.alpha = 0f

            //pictureChanged  = true
        }
    }

    private fun uploadImageToFirebaseStorage() {

//        if (selectedPhotoUri == null) {
//            //saveUserToFirebaseDatabase(token)
//        } else {
//            viewModel.uploadImageToFirebaseStorage(selectedPhotoUri!!)
//            viewModel.isUploadImageSuccessful.observe(this, Observer { isUploadImageSuccessful ->
//                if (isUploadImageSuccessful) {
//                    viewModel.uploadedImageUri.observe(this, Observer { uploadedImageUri ->
//                        mUploadedImageUri = uploadedImageUri
//                    })
//                } else {
//                    viewModel.uploadImageErrorMessage.observe(this, Observer {
//                        toast("Failed to upload image to storage: $it")
//                    })
//                }
//            })
//        }


//        val editTextUsername = editTextProfile.text.toString()
//
//        if (editTextUsername.isEmpty()) {
//            toast("Please fill out username")
//            return
//        }
//
        if (selectedPhotoUri == null) return
        viewModel.uploadImageToFirebaseStorage(selectedPhotoUri!!)
        viewModel.isUploadImageSuccessful.observe(this, Observer { isUploadImageSuccessful ->
            if (isUploadImageSuccessful) {
                viewModel.uploadedImageUri.observe(this, Observer { uploadedImageUri ->
                    mUploadedImageUri = uploadedImageUri
                })
            } else {
                viewModel.uploadImageErrorMessage.observe(this, Observer {
                    longToast("Failed to upload image to storage: $it")
                })
            }
        })

//        val filename = UUID.randomUUID().toString()
//        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
//
//        ref.putFile(selectedPhotoUri!!)
//            .addOnSuccessListener {
//                Log.d(RegisterActivity.TAG, "Successfully uploaded image: ${it.metadata?.path}")
//
//                ref.downloadUrl.addOnSuccessListener {
//                    Log.d(RegisterActivity.TAG, "File Location: $it")
//                    //Picasso.get().load(it).into(circleImageViewMain)
//                    updateProfile(it.toString())
//                }
//            }
//            .addOnFailureListener {
//                Log.d(RegisterActivity.TAG, "Failed to upload image to storage: ${it.message}")
//            }
    }

    private fun updateProfile(profileImageUrl: String) {

        viewModel.updateProfile(profileImageUrl, editTextProfile.text.toString())
        viewModel.isSuccessful.observe(this, androidx.lifecycle.Observer { isSuccessful ->
            if(isSuccessful){
                toast("Profile successfully updated!")
                circleImageViewProfile.setImageBitmap(bitmap)
            }
        })

//        val uid = FirebaseAuth.getInstance().uid ?: ""
//        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//
//        val user = Profile(uid, editTextProfile.text.toString(), profileImageUrl)
//
//        ref.setValue(user)
//            .addOnSuccessListener {
//                //Log.d(RegisterActivity.TAG, "Profile successfully updated!")
//                Toast.makeText(this, "Profile successfully updated!", Toast.LENGTH_LONG).show()
//                //circleImageViewMain.setImageBitmap(bitmap)
//            }
//            .addOnFailureListener {
//                //Log.d(RegisterActivity.TAG, "Failed to update value to database: ${it.message}")
//                Toast.makeText(this, "Profile successfully updated!", Toast.LENGTH_LONG).show()
//            }
    }
}
