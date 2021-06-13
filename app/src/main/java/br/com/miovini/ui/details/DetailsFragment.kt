package br.com.miovini.ui.details

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.miovini.R
import br.com.miovini.databinding.DetailsFragmentBinding
import br.com.miovini.databinding.DialogCommentDetailsWineBinding
import br.com.miovini.models.genetareCommentist
import br.com.miovini.models.genetareVintageist
import br.com.miovini.ui.details.adapter.CommentAdapter
import br.com.miovini.ui.details.adapter.VintageAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class DetailsFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: DetailsFragmentBinding? = null
    private lateinit var viewModel: DetailsViewModel

    private suspend fun loadUserData() {
        return coroutineScope {
            withContext(Dispatchers.Default) {
                commentAdapter.submitList(genetareCommentist())
                vintageAdapter.submitList(genetareVintageist())

                withContext(Dispatchers.Main) {
                    commentAdapter.notifyDataSetChanged()
                    vintageAdapter.notifyDataSetChanged()
                    Log.d("Testando", binding.viewFlipperDetailsWine.childCount.toString())
                    binding.viewFlipperDetailsWine.displayedChild = 1
                }
            }
        }
    }

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter().apply {
            commentItemClickListener = { comment ->
                val binding = DialogCommentDetailsWineBinding.inflate(layoutInflater)
                val dialog = MaterialAlertDialogBuilder(
                    requireContext()
                ).setView(binding.root).create()

                binding.textView26.text = comment.comment

                with(binding.topAppBar) {
                    title = "Vinho 1"
                    subtitle = comment.data
                    setNavigationOnClickListener { dialog.dismiss() }
                }

                dialog.show()
            }
        }
    }
    private val vintageAdapter: VintageAdapter by lazy { VintageAdapter() }

    private var isBookmark = false
    private var sneackBar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(
        view: View, savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)

        binding.contentDetails.rvComments.run {
            setHasFixedSize(true)
            adapter = commentAdapter
        }

        binding.contentDetails.rvVintage.run {
            setHasFixedSize(true)
            adapter = vintageAdapter
        }

        binding.contentDetails.bntBookmark.setOnClickListener {
            changeIconBookMarks()

            if (sneackBar == null) sneackBar = createSnackBar()

            sneackBar?.setText(
                if (isBookmark) getString(R.string.text_added_to_bookmarks)
                else getString(R.string.text_removed_of_bookmarks)
            )?.show()
        }

        GlobalScope.launch { loadUserData() }
    }

    override fun onDestroy() {
        super.onDestroy()
        sneackBar?.dismiss()
    }

    private fun changeIconBookMarks() {
        binding.contentDetails.bntBookmark.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                if (isBookmark) R.drawable.bookmark_add_24 else R.drawable.bookmark_remove_24
            )
        )

        isBookmark = !isBookmark
    }

    private fun createSnackBar(): Snackbar {
        return Snackbar.make(
            requireView(), "text", Snackbar.LENGTH_SHORT
        ).apply {
            view.findViewById<TextView>(R.id.snackbar_text).setTextSize(
                TypedValue.COMPLEX_UNIT_SP, 15f
            )

            setAction(R.string.text_undo_snackbar) { changeIconBookMarks() }

            setActionTextColor(
                ContextCompat.getColor(requireContext(), R.color.colorOnSecondaryNight)
            )
            setTextColor(ContextCompat.getColor(requireContext(), R.color.colorOnSecondaryNight))
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.colorSecondaryNight))
        }
    }
}