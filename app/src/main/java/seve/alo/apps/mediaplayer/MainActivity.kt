package seve.alo.apps.mediaplayer

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import java.io.IOException

class MainActivity : AppCompatActivity(), MediaPlayer.OnPreparedListener {

    var mp : MediaPlayer? = null
    val CODIGO_GRABAR = 50
    var uri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val buttonGrabar = findViewById<Button>(R.id.buttonGrabar)
        val buttomReproduccir = findViewById<Button>(R.id.buttonReproduccir)

        button!!.setOnClickListener {  sonido() }
        buttonGrabar!!.setOnClickListener { grabar() }
        buttomReproduccir!!.setOnClickListener { reproduccirGrabacion() }
    }

    // --- Audio con Media Player --- //
    fun sonido(){
        // Media Player
        val mediaPlayer = MediaPlayer.create(this, R.raw.animals020)
        mediaPlayer.start()
    }

    // --- Audio en la nube --- //
    fun audioNube(){
        mp = MediaPlayer()

        // --- Audio en la nube Async --- //
        // Le decimos que el MainActivity estara escuchando
        mp!!.setOnPreparedListener(this)

        try {
            mp!!.setDataSource("http://algunapagina/algomas.com")

            // --- preparar asíncronamente --- //
            mp!!.prepareAsync() // Preparamos asincronamente
            // mp.prepare()     // Preparamos de manera manual, no es asincrono
            // mp.start()       // Arrancamos el audio de forma manual, no es asincrono
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /// --- Audio de la interfaz OnPreparedListener --- //

    override fun onPrepared(p0: MediaPlayer?) {
       mp!!.start()
    }

    // --- Audio Nativo --- //
    fun reproductorNativo() {
        val intent = Intent(Intent.ACTION_VIEW)

        // Acceder a la SD card
        val  data = Uri.parse("file:///sdcard" + "sonido.mp3")

        // Acceder desde memoria interna
        //val data = Uri.parse("android.resource://" + packageName + "/" + R.raw.animals020)

        getIntent().setDataAndType(data, "audio/mp3")
        startActivity(intent)
    }

    // --- Grabar Audio --- //
    fun grabar() {
        val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)
        // Esta deprecado
        startActivityForResult(intent, CODIGO_GRABAR)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CODIGO_GRABAR && resultCode == Activity.RESULT_OK) {
            uri = data!!.data
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // --- Reproducción del Audio --- //
    fun reproduccirGrabacion(){
        val mp = MediaPlayer.create(this, uri)
        mp.start()
    }
}