package com.example.chatting_application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chatting_application.databinding.ActivityMainBinding
import com.example.chatting_application.login.LogIn

class MainActivity : AppCompatActivity(){
    companion object {
        private val TAG: String = UtilConstant.PREFIX + MainActivity::class.java.simpleName
    }

    private lateinit var adapter: Adapter

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initUI()
        initData()

    }

    private fun initUI() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this, MainViewModelFactory(this))[MainViewModel::class.java]
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initData() {
        viewModel.getUserList().observe(this) {
            it?.let {
                Log.i(TAG, "list size: ${it.size}")
                adapter = Adapter(it)
                binding.recyclerView.adapter = adapter
            }
        }

        viewModel.getLoginLiveData().observe(this) {
            if (it != null && !it) {
                val intent = Intent(this, LogIn::class.java)
                finish()
                startActivity(intent)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.log_out) {
            Log.i(TAG, "Logging out............")
            viewModel.logOut()
            return true
        }
        return true
    }
}