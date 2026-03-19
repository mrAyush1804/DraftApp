package com.example.draftapp.feature.home.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.draftapp.R
import com.example.draftapp.feature.EditDraft.Presentation.EdraftActivity
import com.example.draftapp.feature.home.data.User
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class homeactivity : AppCompatActivity() {

    private val viewModel: DraftListViewModel by viewModels()
    private lateinit var adapter: DraftAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homeactivity)

        setupUserSpinner()
        setupRecyclerView()
        setupListeners()
        observeState()
    }

    private fun setupUserSpinner() {
        val spinner = findViewById<Spinner>(R.id.spinnerUsers)
        val userAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, viewModel.users)
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = userAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedUser = viewModel.users[position]
                viewModel.switchUser(selectedUser)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupRecyclerView() {
        adapter = DraftAdapter(
            onDraftClick = { draft ->
                val intent = Intent(this, EdraftActivity::class.java).apply {
                    putExtra("DRAFT_ID", draft.id)
                    putExtra("USER_ID", viewModel.currentUser.value.id)
                }
                startActivity(intent)
            },
            onLockClick = { draft ->
                viewModel.toggleLock(draft.id, draft.Locked)
            }
        )
        findViewById<RecyclerView>(R.id.rvDrafts).apply {
            layoutManager = LinearLayoutManager(this@homeactivity)
            adapter = this@homeactivity.adapter
        }
    }

    private fun setupListeners() {
        findViewById<ExtendedFloatingActionButton>(R.id.fabNewDraft).setOnClickListener {
            val intent = Intent(this, EdraftActivity::class.java).apply {
                putExtra("USER_ID", viewModel.currentUser.value.id)
            }
            startActivity(intent)
        }
    }

    private fun observeState() {
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        val emptyState = findViewById<TextView>(R.id.tvEmptyState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.draftListState.collect { state ->
                    when (state) {
                        is Handlehomestate.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Handlehomestate.Success -> {
                            progressBar.visibility = View.GONE
                            adapter.submitList(state.data)
                            emptyState.visibility = if (state.data.isEmpty()) View.VISIBLE else View.GONE
                        }
                        is Handlehomestate.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(this@homeactivity, state.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
