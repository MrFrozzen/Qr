package ru.mrfrozzen.qrcode.ui.generate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_qrgenerate.*
import net.glxn.qrgen.core.scheme.VCard
import ru.mrfrozzen.qrcode.R
import java.io.File
import java.io.FileOutputStream

class QRGenerateFragment : Fragment(), View.OnClickListener{

    private var qrImage : Bitmap? = null
    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_qrgenerate, container, false)
        val btn_text = root.findViewById(R.id.btn_text) as Button
        val btn_vCard = root.findViewById(R.id.btn_vCard) as Button
        val btn_save = root.findViewById(R.id.btn_save) as Button
        val btn_generateQR = root.findViewById(R.id.btn_generateQR) as Button
        btn_text.setOnClickListener(this)
        btn_vCard.setOnClickListener(this)
        btn_save.setOnClickListener(this)
        btn_generateQR.setOnClickListener(this)

        if (!checkPermissionForExternalStorage()) {
            requestPermissionForExternalStorage()
        }
        return root
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btn_text->
            {
                input_text.visibility = View.VISIBLE
                layout_vCard.visibility = View.GONE
                btn_generateQR.visibility = View.VISIBLE
            }
            R.id.btn_vCard->
            {
                input_text.visibility = View.GONE
                layout_vCard.visibility = View.VISIBLE
                btn_generateQR.visibility = View.VISIBLE

            }
            R.id.btn_generateQR->
            {
                if(layout_vCard.visibility == View.VISIBLE) {

                    if(input_name.text.toString().isNullOrEmpty() && input_email.text.toString().isNullOrEmpty()
                        && input_address.text.toString().isNullOrEmpty() && input_phoneNumber.text.toString().isNullOrEmpty()
                        && input_website.text.toString().isNullOrEmpty())
                    {
                        Toast.makeText(context,R.string.gen_toast_all_fields_empty , Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        generateQRCode()
                    }
                }
                else if(input_text.visibility == View.VISIBLE) {
                    if(!input_text.text.toString().isNullOrEmpty())
                    {
                        generateQRCode()
                    }
                    else
                    {
                        input_text.error = R.string.gen_toast_this_field_is_required.toString()
                    }
                }
            }
            R.id.btn_save->
            {
                if (!checkPermissionForExternalStorage()) {
                    Toast.makeText(activity, R.string.gen_toast_external_storage_permission_needed, Toast.LENGTH_LONG).show()
                }
                else
                {
                    if(qrImage != null){saveImage(qrImage!!)}
                }
            }
        }
    }

    //Function for Generating QR code
    private fun generateQRCode()
    {
        if(layout_vCard.visibility == View.VISIBLE)
        {
            val vCard = VCard(input_name.text.toString())
                .setEmail(input_email.text.toString())
                .setAddress(input_address.text.toString())
                .setPhoneNumber(input_phoneNumber.text.toString())
                .setWebsite(input_website.text.toString())
            qrImage =
                net.glxn.qrgen.android.QRCode.from(vCard).bitmap()
            if(qrImage != null)
            {
                imageView_qrCode.setImageBitmap(qrImage)
                btn_save.visibility = View.VISIBLE
            }
        }
        else if(input_text.visibility == View.VISIBLE)
        {
            qrImage = net.glxn.qrgen.android.QRCode.from(input_text.text.toString()).bitmap()
            if(qrImage != null)
            {
                imageView_qrCode.setImageBitmap(qrImage)
                btn_save.visibility = View.VISIBLE
            }
        }
    }

    //function for requesting storage access
    private fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Toast.makeText(activity, R.string.gen_toast_external_storage_permission_needed, Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(context as Activity, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
        }
    }

    //fuunction for checking storage permission
    private fun checkPermissionForExternalStorage(): Boolean {

        val result = ContextCompat.checkSelfPermission(context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    //funtion for saving image into gallery
    private fun saveImage(image: Bitmap): String {
        var savedImagePath: String? = null

        val imageFileName = "QR" + getTimeStamp() + ".jpg"
        val storageDir = File(
            activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/QRGenerator")
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileName)
            savedImagePath = imageFile.absolutePath
            try {
                val fOut = FileOutputStream(imageFile)
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Add the image to the system gallery
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val f = File(savedImagePath)
            val contentUri = Uri.fromFile(f)
            mediaScanIntent.data = contentUri
            activity?.sendBroadcast(mediaScanIntent)
            Toast.makeText(activity,"QR Image saved into folder: $storageDir",Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity,R.string.gen_toast_ERROR_SAVING_IMAGE,Toast.LENGTH_SHORT).show()
        }
        return savedImagePath!!
    }


    private fun getTimeStamp(): String? {
        val tsLong = System.currentTimeMillis() / 1000

        return tsLong.toString()
    }
}

