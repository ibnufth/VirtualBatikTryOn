package com.dimensicodes.virtualbatiktryon.ui.pattern

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import coil.request.ImageRequest
import com.dimensicodes.virtualbatiktryon.R
import com.dimensicodes.virtualbatiktryon.data.dataupload.domainConnect
import com.dimensicodes.virtualbatiktryon.data.source.local.remote.response.BatikItem
import com.dimensicodes.virtualbatiktryon.data.source.local.remote.response.OriginItem
import com.dimensicodes.virtualbatiktryon.databinding.FragmentPatternBinding
import com.dimensicodes.virtualbatiktryon.ui.detail.DetailFragment
import com.dimensicodes.virtualbatiktryon.ui.gender.GenderFragment
import com.dimensicodes.virtualbatiktryon.viewmodel.ViewModelFactory
import com.kumastudio.cameraapp.dataupload.UploadUtility
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import coil.imageLoader

class PatternFragment : Fragment() {
    private lateinit var patternFragmentBinding : FragmentPatternBinding
    private lateinit var patternViewModel: PatternViewModel
    private val batikList = ArrayList<BatikItem>()
    private val originBatikList = ArrayList<OriginItem>()
    private val mOrigin = ArrayList<String>()
    private lateinit var rvAdapter : GridBatikAdapter
    private lateinit var image: ImageView
    private lateinit var imageData: Bitmap
    private lateinit var clothesImage: Bitmap
    private lateinit var normalMapImage: Bitmap
    private var imagePath: String? = null
    private val resultPhoto = MutableLiveData<Bitmap>()
    private var batikSelected = MutableLiveData<BatikItem>()
    companion object{
        private const val TAG = "PatternFragment"
        const val EXTRA_MESSAGE = "CAMERA_IMAGE"
        const val EXTRA_GENDER= "extra gender"
        const val EXTRA_BATIK= "extra batik"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        patternFragmentBinding = FragmentPatternBinding.inflate(inflater,container,false)
        return patternFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        patternViewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity()))[PatternViewModel::class.java]

        patternFragmentBinding.rvBatik.setHasFixedSize(true)
        patternViewModel.getOrigin().observe(viewLifecycleOwner,{origin->
            Log.d(TAG, "onViewCreated: origin ->$origin")
            originBatikList.addAll(origin)
            for (i in origin.indices){
                mOrigin.add(origin[i].name!!)
            }
            spinnerBatikList()
        })

        imagePath = arguments?.getString(EXTRA_MESSAGE)
        Log.d(GenderFragment.TAG, "onViewCreated argument: $imagePath")
        Picasso.with(context).load("file:///$imagePath").into(
            object : Target {
                override fun onBitmapFailed(errorDrawable: Drawable) {}
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    if (bitmap != null) {
                        imageData = bitmap.copy(Bitmap.Config.ARGB_8888, true);//image yang difoto
                        clothesImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                        normalMapImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                        uploadImage(bitmapToFile(bitmap,"image.png"),imageData)
                    }
                    image = view.rootView.findViewById(R.id.img_source)
                    image.setImageBitmap(bitmap)
                }
            }
        )

        patternFragmentBinding.btnProcess.setOnClickListener {
            val mDetailFragment = DetailFragment()
            lateinit var bitmap: Bitmap
            val mBundle = Bundle()
            var gender = arguments?.getString(EXTRA_GENDER)
            resultPhoto.observe(viewLifecycleOwner,{batik ->
                bitmap = batik
            })
            var batik = String()
            batikSelected.observe(viewLifecycleOwner,{ data ->
                batik=data.name!!
            })
            mBundle.putParcelable(EXTRA_MESSAGE, bitmap)
            mBundle.putString(EXTRA_GENDER,gender)
            mBundle.putString(EXTRA_BATIK,batik)
            mDetailFragment.arguments = mBundle
            val mFragmentManager = fragmentManager
            mFragmentManager?.beginTransaction()?.apply {
                replace(R.id.frame_container,mDetailFragment,DetailFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }

    }

    private fun spinnerBatikList() {
        val adapter = ArrayAdapter(activity as Context, android.R.layout.simple_spinner_dropdown_item, mOrigin)
        patternFragmentBinding.searchBox.adapter = adapter
        patternFragmentBinding.searchBox.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                batikList.clear()
                patternViewModel.getBatik().observe(viewLifecycleOwner,{ batik ->
                    Log.d(TAG, "onViewCreated: batik -> $batik")
                    for (i in batik.indices){
                        val batikOrigin = ArrayList<BatikItem>()
                        for (k in batik[i].origin!!){
                            Log.d(TAG, "onItemSelected: id origin $k")
                            Log.d(TAG, "onItemSelected: id ${originBatikList[position].id}")
                            if (k==originBatikList[position].id){
                                Log.d(TAG, "onItemSelected: batik insert ${batik[i]}")
                                batikOrigin.add(batik[i])
                            }
                        }
                        Log.d(TAG, "onItemSelected batik origin: $batikOrigin")
                            batikList.addAll(batikOrigin)
                            showRecyclerView()
                    }

                })
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }
    }

    private fun showRecyclerView(){
        patternFragmentBinding.rvBatik.layoutManager = GridLayoutManager(context, 4)
        rvAdapter = GridBatikAdapter()
        rvAdapter.listBatik.addAll(batikList)
        patternFragmentBinding.rvBatik.adapter = rvAdapter
        rvAdapter.setOnItemClickCallBack(object : GridBatikAdapter.OnitemCLickCallback{
            override fun onItemClicked(data: BatikItem) {
                batikSelected.postValue(data)
            }

        })
    }

    fun uploadImage(sourceFile: File?, realPhoto: Bitmap){
        sourceFile?.let{
            val runFunc = { filename:String ->
                Log.d(TAG,"program runnig baby")
                processBatik(activity as Activity,filename.substringAfterLast('/'),realPhoto)
            }
            UploadUtility(activity as Activity).uploadFile(it, null, runFunc)
        }
    }
    fun bitmapToFile(bitmap: Bitmap, fileNameToSave: String): File? { // File name like "image.png"
        //create a file to write bitmap data
        var file: File? = null
        return try {
            file = File(Environment.getExternalStorageDirectory().toString() + File.separator + fileNameToSave)
            file.createNewFile()

            //Convert bitmap to byte array
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos) // YOU can also save it in JPEG
            val bitmapdata = bos.toByteArray()

            //write the bytes in file
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            file // it will return null
        }
    }
    private fun Bitmap.getBatikMasking(clothes:Bitmap, normal:Bitmap, batik:Bitmap, backgroundColor:Int = Color.WHITE):Bitmap?{
        val bitmap = copy(config,true)
        Log.d(TAG, "real photo $width $height")
        Log.d(TAG,"cloth photo "+clothes.width+" "+clothes.height)
        Log.d(TAG,"normals map photo"+normal.width+" "+normal.height)
        var alpha:Int
        var pixel:Int
        val newBatik = batik.resizeByWidth(width)
        Log.d(TAG,""+newBatik.width+" "+newBatik.height)
        Log.d(TAG,"batik painting start")
        // scan through all pixels
        for (x in 0 until width){
            for (y in 0 until height){
                pixel = clothes.getPixel(x,y)
                alpha = Color.alpha(pixel)
                if (alpha > 100){
                    val normalHsv = FloatArray(3)
                    Color.colorToHSV(normal.getPixel(x,y), normalHsv)
                    val kecerahanbatik = 1-((normalHsv[0]-120)/255)*normalHsv[2]
                    val batikHsv = FloatArray(3)
                    Color.colorToHSV(newBatik.getPixel(if(x<newBatik.width) x else (x%newBatik.width),if(y<newBatik.height) y else (y%newBatik.height)), batikHsv)
                    batikHsv[2] = kecerahanbatik
                    bitmap.setPixel(x,y, Color.HSVToColor(batikHsv))
                }
            }
        }
        Log.d(TAG,"image changed")
        return bitmap
    }
    private fun Bitmap.resizeByWidth(width:Int):Bitmap{
        val ratio:Float = this.width.toFloat() / this.height.toFloat()
        val height:Int = Math.round(width / ratio)
        val newWidth = Math.round(height / ratio)
        return Bitmap.createScaledBitmap(
            this,
            newWidth,
            height,
            false
        )
    }
    fun getImageUri(inImage : Bitmap) : Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(context?.contentResolver, inImage, "Title", null)
        return Uri.parse(path.toString())
    }

    private fun getImageBitmapUrl(url:String, func: (m: Drawable?) -> Unit){
        val request = ImageRequest.Builder(requireContext())
            .data(url)
            .target { drawable ->
                func(drawable)
            }
            .build()
        val disposable = context?.imageLoader?.enqueue(request)

    }
    private fun processBatik(actIn: Activity, name:String, realPhoto: Bitmap){
        var batik = String()
        batikSelected.observe(viewLifecycleOwner,{ data ->
            batik=data.imagePath!!
        })
        val inputSystem  = context?.assets
        var cloth = BitmapFactory.decodeFile("$inputSystem$batik")
        var normals = BitmapFactory.decodeFile("$inputSystem$batik")
        val url = domainConnect.serverURL

        val toggleMessage = UploadUtility(actIn)
        val batikPhoto = BitmapFactory.decodeFile("$inputSystem$batikSelected")
        val paintBatik = {
            toggleMessage.toggleProgressDialog(true,"paint batik")
            clothesImage = cloth
            normalMapImage = normals
            paintBatik(batikPhoto)
            toggleMessage.toggleProgressDialog(false)
        }
        val getNormal = {
            Log.d(TAG,"get normal map start")
            UploadUtility(activity as Activity).getUrlfromWeb("normalmap/$name",{
                val urlstr = it
                Timer().schedule(3000) {
                    getImageBitmapUrl(urlstr,{
                        it?.let{
                            Log.d(TAG,"get normal map end")
                            normals = it.toBitmap()
                            resultPhoto.postValue(normals)
                            Log.d(TAG,normals.height.toString())
                            paintBatik()
                        }
                    })
                }
            })
        }
        val rembg = {
            Log.d(TAG,"remove background start")
            UploadUtility(activity as Activity).getUrlfromWeb("rembg/$name",{
                val urlstr = it
                Timer().schedule(3000) {
                    getImageBitmapUrl(urlstr, {
                        it?.let {
                            cloth = it.toBitmap()
                            resultPhoto.postValue(cloth)
                            getNormal()
                        }
                    })
                }
            })
        }
        Log.d(TAG, "get clothes from getcloth/$name")
        val getclothes = {
            UploadUtility(activity as Activity).getUrlfromWeb("getcloth/$name",{
                val urlstr = it
                Timer().schedule(3000){
                    getImageBitmapUrl(urlstr,{
                        it?.let{
                            rembg()
                        }
                    })
                }
            })
        }
        getclothes()
        //paintBatik()
    }
    fun paintBatik(batikPilihan: Bitmap){
        val clothesImage = clothesImage.copy(Bitmap.Config.ARGB_8888,true);
        val normalImage = normalMapImage.copy(Bitmap.Config.ARGB_8888,true);
        val paintbatik = imageData.getBatikMasking(clothesImage,normalImage,batikPilihan)
        resultPhoto.postValue(paintbatik)
    }

}