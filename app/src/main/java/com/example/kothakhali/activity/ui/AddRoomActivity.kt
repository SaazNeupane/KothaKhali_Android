package com.example.kothakhali.activity.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.kothakhali.R
import com.example.kothakhali.activity.model.AdList
import com.example.kothakhali.activity.notificatons.NotificationChannels
import com.example.kothakhali.activity.repository.AdRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddRoomActivity : AppCompatActivity() {
    private lateinit var ettitle: EditText
    private lateinit var etpradesh: EditText
    private lateinit var etdistrict: EditText
    private lateinit var etcity: EditText
    private lateinit var etstreet: EditText
    private lateinit var etrent: EditText
    private lateinit var etdescription: EditText
    private lateinit var spcategory: Spinner
    private lateinit var rg :RadioGroup
    private lateinit var rbyes: RadioButton
    private lateinit var rbno: RadioButton
    private lateinit var image: ImageView
    private lateinit var btnaddroom: Button
    private var selectedcategory = ""

    private val category= arrayOf(
        "Room","Hostel","House","Apartment","Shop","Office"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)
        ettitle=findViewById(R.id.ettitle)
        etpradesh=findViewById(R.id.etpradesh)
        etdistrict=findViewById(R.id.etdistrict)
        etcity=findViewById(R.id.etcity)
        etstreet=findViewById(R.id.etstreet)
        etrent=findViewById(R.id.etrent)
        etdescription=findViewById(R.id.etdescription)
        spcategory=findViewById(R.id.spcategory)
        rg = findViewById(R.id.rg)
        rbyes=findViewById(R.id.rbyes)
        rbno=findViewById(R.id.rbno)
        image=findViewById(R.id.image1)
        btnaddroom=findViewById(R.id.btnaddroom)
        //Radio Button
        rg.check(R.id.rbyes)

        //Spinner
        val adapter= ArrayAdapter<String>(
            applicationContext,
            R.layout.spinnerliststyle, category
        )
        spcategory.adapter=adapter

        spcategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long) {
                    selectedcategory = parent?.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }

        //Image
        image.setOnClickListener {
            loadpopup()
        }

        //button
        btnaddroom.setOnClickListener {
            if (checkEmpty()){
                addroom()
            }
        }
    }

    private fun addroom(){
        val adtitle=ettitle.text.toString()
        val pradesh=etpradesh.text.toString()
        val district=etdistrict.text.toString()
        val city = etcity.text.toString()
        val street = etstreet.text.toString()
        val roomcategory=selectedcategory
        val rent=etrent.text.toString()
        var negotiable = ""
        if(rbyes.isChecked){
            negotiable = rbyes.text.toString()
        }
        else if (rbno.isChecked){
            negotiable= rbno.text.toString()
        }
        val description=etdescription.text.toString()

        val ad = AdList(
            adtitle =adtitle,
            pradesh = pradesh,
            district = district,
            city = city,
            street = street,
            category = roomcategory,
            rent = rent,
            negotiable = negotiable,
            description = description)

        CoroutineScope(Dispatchers.IO).launch{
            try{
                val adRepository = AdRepository()
                val response = adRepository.addad(ad)
                if (response.success == true){
                    if (imageUrl != null){
                        uploadImage(response.data!!._id)
                        withContext(Dispatchers.Main){
                            showNotification("${response.message}")
                            startActivity(
                                    Intent(
                                            this@AddRoomActivity,
                                            DashboardActivity::class.java
                                    )
                            )
                        }
                    }
                }
            }
            catch (ex:Exception){
                withContext(Dispatchers.Main){
                    Toast.makeText(this@AddRoomActivity,
                            ex.toString(),
                            Toast.LENGTH_SHORT)
                            .show()
                }
            }
        }
    }

    //Image Load
    private fun loadpopup(){
            val popupMenu = PopupMenu(this, image)
            popupMenu.menuInflater.inflate(R.menu.gallery_camera, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menuCamera ->
                        openCamera()
                    R.id.menuGallery ->
                        openGallery()
                }
                true
            }
            popupMenu.show()
    }
    private var REQUEST_GALLERY_CODE = 0
    private var REQUEST_CAMERA_CODE = 1
    private var imageUrl: String? = null

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY_CODE)
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_GALLERY_CODE && data != null) {
                val selectedImage = data.data
                val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                val contentResolver = contentResolver
                val cursor =
                        contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                cursor!!.moveToFirst()
                val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                imageUrl = cursor.getString(columnIndex)
                image.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
                cursor.close()
            } else if (requestCode == REQUEST_CAMERA_CODE && data != null) {
                val imageBitmap = data.extras?.get("data") as Bitmap
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val file = bitmapToFile(imageBitmap, "$timeStamp.jpg")
                imageUrl = file!!.absolutePath
                image.setImageBitmap(BitmapFactory.decodeFile(imageUrl))
            }
        }

    }

    private fun uploadImage(clientid: String) {
        if (imageUrl != null) {
            val file = File(imageUrl!!)
            val reqFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val body =
                    MultipartBody.Part.createFormData("file", file.name, reqFile)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val adRepository = AdRepository()
                    val response = adRepository.uploadImage(clientid, body)
                    if (response.success == true) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@AddRoomActivity, "Uploaded", Toast.LENGTH_SHORT)
                                    .show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.d("Image Error: ", ex.localizedMessage)
                        Toast.makeText(this@AddRoomActivity,
                                ex.localizedMessage,
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun bitmapToFile(
            bitmap: Bitmap,
            fileNameToSave: String
    ): File? {
        var file: File? = null
        return try {
            file = File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                            .toString() + File.separator + fileNameToSave
            )
            file.createNewFile()
            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitMapData = bos.toByteArray()
            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitMapData)
            fos.flush()
            fos.close()
            file
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }

    private fun showNotification(message: String) {
        val notificationManager = NotificationManagerCompat.from(this)

        val notificationChannels = NotificationChannels(this)
        notificationChannels.createNotificationChannels()

        val notification = NotificationCompat.Builder(this, notificationChannels.CHANNEL_1)
                .setSmallIcon(R.drawable.notifications)
                .setContentTitle("KothaKhali")
                .setContentText("$message")
                .setColor(Color.BLUE)
                .build()

        notificationManager.notify(1, notification)
    }

    private fun checkEmpty(): Boolean{
        var flag=true

        if(TextUtils.isEmpty(ettitle.text)){
            ettitle.setError("Please enter Ad Title")
            ettitle.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etpradesh.text)){
            etpradesh.setError("Please enter Pradesh Number")
            etpradesh.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etdistrict.text)){
            etdistrict.setError("Please enter district")
            etdistrict.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etcity.text)){
            etcity.setError("Please enter city")
            etcity.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etstreet.text)){
            etstreet.setError("Please enter street")
            etstreet.requestFocus()
            flag = false
        }
        else if(TextUtils.isEmpty(etrent.text)){
            etrent.setError("Please Enter Rent")
            etrent.requestFocus()
            flag = false
        }
        return flag
    }
}