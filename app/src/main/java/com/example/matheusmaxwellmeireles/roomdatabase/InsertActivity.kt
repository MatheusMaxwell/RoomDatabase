package com.example.matheusmaxwellmeireles.roomdatabase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.matheusmaxwellmeireles.roomdatabase.Model.User
import kotlinx.android.synthetic.main.activity_insert.*

class InsertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)


        btnAdd.setOnClickListener({
            if(!edtNome.text.isEmpty() || !edtEmail.text.isEmpty()){
                var user = User()
                user.name = edtNome.text.toString()
                user.email = edtEmail.text.toString()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)

            }
            else{
                Toast.makeText(this, "Dado(s) em branco.", Toast.LENGTH_LONG).show()
            }
        })
    }
}
