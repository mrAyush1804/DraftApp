package com.example.draftapp.feature.EditDraft.Presentation

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.draftapp.R
import com.example.draftapp.feature.home.data.Draft
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EdraftActivity : AppCompatActivity() {

    private val viewModel: EditDraftViewModel by viewModels()

    private lateinit var etTitle: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var tilTitle: TextInputLayout
    private lateinit var tilDescription: TextInputLayout
    private lateinit var btnSave: MaterialButton
    private lateinit var btnLock: MaterialButton
    private lateinit var tvToolbarTitle: TextView
    private lateinit var ivLockBadge: ImageView
    private lateinit var layoutLockedBanner: View
    private lateinit var layoutLockedFooter: View
    private lateinit var layoutActionButtons: View
    private lateinit var layoutStatusChip: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edraft)

        val userId = intent.getStringExtra("USER_ID") ?: "user_1"
        viewModel.setUserId(userId)

        initViews()
        setupListeners()
        observeState()

        val draftId = intent.getIntExtra("DRAFT_ID", -1)
        viewModel.loadDraft(draftId)
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        tilTitle = findViewById(R.id.tilTitle)
        tilDescription = findViewById(R.id.tilDescription)
        btnSave = findViewById(R.id.btnSave)
        btnLock = findViewById(R.id.btnLock)
        tvToolbarTitle = findViewById(R.id.tvToolbarTitle)
        ivLockBadge = findViewById(R.id.ivLockBadge)
        layoutLockedBanner = findViewById(R.id.layoutLockedBanner)
        layoutLockedFooter = findViewById(R.id.layoutLockedFooter)
        layoutActionButtons = findViewById(R.id.layoutActionButtons)
        layoutStatusChip = findViewById(R.id.layoutStatusChip)
    }

    private fun setupListeners() {
        findViewById<ImageView>(R.id.ivBack).setOnClickListener { finish() }

        btnSave.setOnClickListener {
            viewModel.saveDraft(
                etTitle.text.toString(),
                etDescription.text.toString(),
                isLocked = false
            )
        }

        btnLock.setOnClickListener {
            viewModel.saveDraft(
                etTitle.text.toString(),
                etDescription.text.toString(),
                isLocked = true
            )
        }
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.draftState.collect { state ->
                    when (state) {
                        is EditDraftState.New -> setupNewMode()
                        is EditDraftState.Loaded -> setupEditMode(state.draft)
                        is EditDraftState.Saved -> {
                            Toast.makeText(this@EdraftActivity, "Draft saved", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        is EditDraftState.Error -> {
                            Toast.makeText(this@EdraftActivity, state.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupNewMode() {
        tvToolbarTitle.text = "New Draft"
        ivLockBadge.visibility = View.GONE
        layoutLockedBanner.visibility = View.GONE
        layoutLockedFooter.visibility = View.GONE
        layoutActionButtons.visibility = View.VISIBLE
        btnSave.text = "Save Draft"
        etTitle.isEnabled = true
        etDescription.isEnabled = true
    }

    private fun setupEditMode(draft: Draft) {
        etTitle.setText(draft.title)
        etDescription.setText(draft.description)
        
        if (draft.isLocked) {
            tvToolbarTitle.text = "Locked Draft"
            ivLockBadge.visibility = View.VISIBLE
            ivLockBadge.setImageResource(R.drawable.bglockicon)
            
            layoutLockedBanner.visibility = View.VISIBLE
            layoutLockedFooter.visibility = View.VISIBLE
            layoutActionButtons.visibility = View.GONE
            
            etTitle.isEnabled = false
            etDescription.isEnabled = false
        } else {
            tvToolbarTitle.text = "Edit Draft"
            ivLockBadge.visibility = View.VISIBLE
            
            layoutLockedBanner.visibility = View.GONE
            layoutLockedFooter.visibility = View.GONE
            layoutActionButtons.visibility = View.VISIBLE
            
            etTitle.isEnabled = false 
            etDescription.isEnabled = true
            btnSave.text = "Update Draft"
        }
    }
}
