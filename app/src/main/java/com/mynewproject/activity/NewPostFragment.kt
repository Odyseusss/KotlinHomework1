package com.mynewproject.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mynewproject.databinding.FragmentNewPostBinding
import com.mynewproject.util.AndroidUtils
import com.mynewproject.util.DraftManager
import com.mynewproject.viewmodel.PostViewModel

class NewPostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val isEditing = viewModel.edited.value?.id != 0L

        if (!isEditing) {
            val draft = DraftManager.getDraft(requireContext())
            if (!draft.isNullOrBlank()) {
                binding.content.setText(draft)
            }
        }

        viewModel.edited.observe(viewLifecycleOwner) { post ->
            if (post?.id != 0L) {
                binding.content.setText(post.content)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            val text = binding.content.text?.toString()?.trim()
            val editedId = viewModel.edited.value?.id ?: 0L

            if (!text.isNullOrBlank() && editedId == 0L) {
                DraftManager.saveDraft(requireContext(), text)
            }

            findNavController().navigateUp()
        }

        binding.add.setOnClickListener {
            val content = binding.content.text.toString().trim()
            if (content.isNotBlank()) {
                viewModel.changeContent(content)
                viewModel.save(content)
                DraftManager.clearDraft(requireContext())
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
        }

        AndroidUtils.showKeyboard(binding.content)

        return binding.root
    }
}