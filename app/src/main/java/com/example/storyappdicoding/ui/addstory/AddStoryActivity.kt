package com.example.storyappdicoding.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.storyappdicoding.R
import com.example.storyappdicoding.databinding.ActivityAddStoryBinding
import com.example.storyappdicoding.helper.User
import com.example.storyappdicoding.helper.reduceFileImage
import com.example.storyappdicoding.helper.rotateBitmap
import com.example.storyappdicoding.helper.uriToFile
import com.example.storyappdicoding.ui.camera.CameraActivity
import com.example.storyappdicoding.ui.stories.StoriesActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File



class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var user: User
    private val addViewModel: AddStoryViewModel by viewModels()

    private var getFile: File? = null
    private var result: Bitmap? = null

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.add_story)

        user = intent.getParcelableExtra(EXTRA_USER)!!

        getPermission()
        setClickListener()
        addViewModel.isLoading.observe(this){
            showLoading(it)
        }

    }


    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
        }
        binding.ivAddStory.setImageBitmap(result)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            getFile = myFile
            binding.ivAddStory.setImageURI(selectedImg)
        }
    }

    private fun setClickListener() {
        with(binding){
            btnCamera.setOnClickListener{
                val intent = Intent(this@AddStoryActivity, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }

            btnGallery.setOnClickListener{
                val intent = Intent()
                intent.action = ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Choose a Picture")
                launcherIntentGallery.launch(chooser)
            }

            btnSubmit.setOnClickListener{
                uploadImage()
            }

        }
    }

    private fun getPermission() {
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun uploadImage() {
        if(getFile!=null && binding.tvDescription.text.toString()!=""){
            val file = reduceFileImage(getFile as File)

            val descriptionFormat = binding.tvDescription.text.toString()
            val description = descriptionFormat.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            addViewModel.uploadImage(
                user.token,
                description,
                imageMultipart,
                object : AddStoryViewModel.Status {
                    override fun isSuccess(status: Boolean) {
                        if(status){
                            showAlertDialog()
                        }
                        else{
                            showErrorAlertDialog()
                        }
                    }

                } )
        }
        else{
            Toast.makeText(this@AddStoryActivity, getString(R.string.incomplete_upload), Toast.LENGTH_SHORT).show()
        }

    }


    private fun showAlertDialog() {
        AlertDialog.Builder(this@AddStoryActivity).apply {
            setTitle(getString(R.string.information))
            setMessage(getString(R.string.success_upload))
            setPositiveButton(getString(R.string.back_to_home)) { _, _ ->
                val intent = Intent(this@AddStoryActivity, StoriesActivity::class.java)
                intent.putExtra(StoriesActivity.EXTRA_USER, user)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            create()
            show()


        }
    }
    private fun showErrorAlertDialog() {
        AlertDialog.Builder(this@AddStoryActivity).apply {
            setTitle(getString(R.string.information))
            setMessage(getString(R.string.failed_upload))
            setPositiveButton(getString(R.string.ok)) { _, _ ->
            }
            create()
            show()
        }
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.addProgressBar.visibility = View.VISIBLE
        }
        else{
            binding.addProgressBar.visibility = View.INVISIBLE
        }
    }



    companion object{
        const val EXTRA_USER = "user"
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}