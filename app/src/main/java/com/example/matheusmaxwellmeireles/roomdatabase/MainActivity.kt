package com.example.matheusmaxwellmeireles.roomdatabase

import android.app.ActionBar
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Observable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.matheusmaxwellmeireles.roomdatabase.Database.UserRepository
import com.example.matheusmaxwellmeireles.roomdatabase.Local.UserDataSource
import com.example.matheusmaxwellmeireles.roomdatabase.Local.UserDatabase
import com.example.matheusmaxwellmeireles.roomdatabase.Model.User
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var adapter: ArrayAdapter<*>
    var userList:MutableList<User> = ArrayList()

    private var compositeDisposable:CompositeDisposable?=null
    private var userRepository:UserRepository?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        compositeDisposable = CompositeDisposable()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, userList)
        registerForContextMenu(listUsers)
        listUsers!!.adapter = adapter

        val userDatabase = UserDatabase.getInstance(this)
        userRepository = UserRepository.getInstance(UserDataSource.getInstance(userDatabase.userDAO()))

        loadData()

        fab_add.setOnClickListener({
            /*var intent = Intent(this, InsertActivity::class.java)
            startActivity(intent)
            val user = intent.getSerializableExtra("user") as User*/
            val user = User()
            val layout = LinearLayout(this@MainActivity)
            layout.orientation = LinearLayout.VERTICAL
            val edtName = EditText(this@MainActivity)
            val edtEmail = EditText(this@MainActivity)
            edtName.setText(user.name)
            edtName.hint = "Enter your name"
            edtEmail.setText(user.email)
            edtEmail.hint = "Enter your email"
            layout.addView(edtName)
            layout.addView(edtEmail)
            AlertDialog.Builder(this@MainActivity)
                    .setTitle("Insert")
                    .setMessage("Insert user")
                    .setView(layout)
                    .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                        if (TextUtils.isEmpty(edtName.text.toString()) || TextUtils.isEmpty(edtEmail.text.toString()))
                            return@OnClickListener
                        else{
                            user.name = edtName.text.toString()
                            user.email = edtEmail.text.toString()
                            insertUser(user)
                        }
                    }).setNegativeButton(android.R.string.cancel){
                        dialog, which -> dialog.dismiss()
                    }.create().show()


        })
    }

    private fun insertUser(user:User){
        if(user != null){
            val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e->

                userRepository!!.insertUser(user)
                e.onComplete()
            })
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(Consumer {

                    },
                            io.reactivex.functions.Consumer {
                                throwable-> Toast.makeText(this@MainActivity, ""+throwable.message, Toast.LENGTH_SHORT)
                                    .show()
                            },
                            Action { loadData() })

            compositeDisposable!!.addAll(disposable)
        }
    }

    private fun loadData() {
        val disposable = userRepository!!.allUsers
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({ users -> onGetAllUserSucess(users) })
                {
                    throwable-> Toast.makeText(this@MainActivity, ""+throwable.message, Toast.LENGTH_SHORT)
                        .show()
                }

        compositeDisposable!!.add(disposable)
    }

    private fun onGetAllUserSucess(users: List<User>) {

        userList.clear()
        userList.addAll(users)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.delete -> deleteAllUsers()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteAllUsers() {
        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e->
            userRepository!!.deleteAllUsers()
            e.onComplete()
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {

                },
                        io.reactivex.functions.Consumer {
                            throwable-> Toast.makeText(this@MainActivity, ""+throwable.message, Toast.LENGTH_SHORT)
                                .show()
                        },
                        Action { loadData() })

        compositeDisposable!!.addAll(disposable)
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        menu.setHeaderTitle("Select action:")
        menu.add(Menu.NONE, 0, Menu.NONE, "UPDATE")
        menu.add(Menu.NONE, 1, Menu.NONE, "DELETE")
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem?): Boolean {
        val info = item!!.menuInfo as AdapterView.AdapterContextMenuInfo
        val user = userList[info.position]
        when(item.itemId){
            0
            ->{
                val layout = LinearLayout(this@MainActivity)
                layout.orientation = LinearLayout.VERTICAL
                val edtName = EditText(this@MainActivity)
                val edtEmail = EditText(this@MainActivity)
                edtName.setText(user.name)
                edtName.hint = "Enter your name"
                edtEmail.setText(user.email)
                edtEmail.hint = "Enter your email"
                layout.addView(edtName)
                layout.addView(edtEmail)
                AlertDialog.Builder(this@MainActivity)
                        .setTitle("Edit")
                        .setMessage("Edit user")
                        .setView(layout)
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            if (TextUtils.isEmpty(edtName.text.toString()) || TextUtils.isEmpty(edtEmail.text.toString()))
                                return@OnClickListener
                            else{
                                user.name = edtName.text.toString()
                                user.email = edtEmail.text.toString()
                                updateUser(user)
                            }
                        }).setNegativeButton(android.R.string.cancel){
                            dialog, which -> dialog.dismiss() 
                        }.create().show()
            }
            1
            ->{
                AlertDialog.Builder(this@MainActivity)
                        .setMessage("Do you want to delete " + user.name+"?")
                        .setPositiveButton(android.R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                            deleteUser(user)
                        })
                        .setNegativeButton(android.R.string.cancel){
                            dialog, which -> dialog.dismiss()
                        }.create().show()
            }
            
        }

        return true
    }


    private fun deleteUser(user: User) {

        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e->
            userRepository!!.deleteUser(user)
            e.onComplete()
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {

                },
                        io.reactivex.functions.Consumer {
                            throwable-> Toast.makeText(this@MainActivity, ""+throwable.message, Toast.LENGTH_SHORT)
                                .show()
                        },
                        Action { loadData() })

        compositeDisposable!!.addAll(disposable)

    }

    private fun updateUser(user: User) {

        val disposable = io.reactivex.Observable.create(ObservableOnSubscribe<Any> {e->
            userRepository!!.updateUser(user)
            e.onComplete()
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(Consumer {

                },
                        io.reactivex.functions.Consumer {
                            throwable-> Toast.makeText(this@MainActivity, ""+throwable.message, Toast.LENGTH_SHORT)
                                .show()
                        },
                        Action { loadData() })

        compositeDisposable!!.addAll(disposable)

    }

    override fun onDestroy() {
        compositeDisposable!!.clear()
        super.onDestroy()
    }

}

