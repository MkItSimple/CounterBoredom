package com.mkitsimple.counterboredom.ui.main

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.Profile
import com.mkitsimple.counterboredom.data.models.User
import com.mkitsimple.counterboredom.ui.auth.RegisterActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    companion object {
        fun newInstance() = ProfileFragment()
        var currentUser: User? = null
    }

    private lateinit var viewModel: ProfileViewModel
    private var pictureChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)

        //buttonSaveChanges.isEnabled = false
        Picasso.get().load(MainActivity.currentUser?.profileImageUrl).into(circleImageViewProfile)
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

            pictureChanged  = true
        }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d(RegisterActivity.TAG, "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d(RegisterActivity.TAG, "File Location: $it")
                    //Picasso.get().load(it).into(circleImageViewMain)
                    updateUser(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d(RegisterActivity.TAG, "Failed to upload image to storage: ${it.message}")
            }
    }

    private fun updateUser(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = Profile(uid, editTextProfile.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                //Log.d(RegisterActivity.TAG, "Profile successfully updated!")
                Toast.makeText(this, "Profile successfully updated!", Toast.LENGTH_LONG).show()
                //circleImageViewMain.setImageBitmap(bitmap)
            }
            .addOnFailureListener {
                //Log.d(RegisterActivity.TAG, "Failed to update value to database: ${it.message}")
                Toast.makeText(this, "Profile successfully updated!", Toast.LENGTH_LONG).show()
            }
    }
}
