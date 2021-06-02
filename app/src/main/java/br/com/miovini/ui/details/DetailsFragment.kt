package br.com.miovini.ui.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.miovini.R
import br.com.miovini.databinding.DetailsFragmentBinding
import br.com.miovini.models.genetareCommentist
import br.com.miovini.models.genetareVintageist
import br.com.miovini.ui.activity.MainActivity
import br.com.miovini.ui.details.adapter.CommentAdapter
import br.com.miovini.ui.details.adapter.VintageAdapter
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: DetailsFragmentBinding? = null
    private lateinit var viewModel: DetailsViewModel

    private val commentAdapter: CommentAdapter by lazy {
        CommentAdapter().apply {
            submitList(genetareCommentist())
            commentItemClickListener = { comment ->
                Log.d("Testando", comment.toString())
            }
        }
    }

    private val vintageAdapter: VintageAdapter by lazy {
        VintageAdapter().apply { submitList(genetareVintageist()) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        binding.rvComments.run {
            setHasFixedSize(true)
            adapter = commentAdapter
        }
        binding.rvVintage.run {
            setHasFixedSize(true); adapter = vintageAdapter
        }

        var isBookmark = false
        binding.bntBookmark.setOnClickListener {
            binding.bntBookmark.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (isBookmark) R.drawable.bookmark_add_24 else R.drawable.bookmark_remove_24
                )
            )

            isBookmark = !isBookmark

            val text = if (isBookmark) "Adicionado aos Favoritos" else "Removido dos Favoritos"

            Snackbar.make(
                requireView(), text, Snackbar.LENGTH_SHORT
            ).apply {
                anchorView = MainActivity.navigationMenu
                setAction("Undo") {
                    binding.bntBookmark.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            if (isBookmark) R.drawable.bookmark_add_24 else R.drawable.bookmark_remove_24
                        )
                    )
                    isBookmark = !isBookmark
                }
                setBackgroundTint(
                    ContextCompat.getColor(requireContext(), R.color.colorSecondaryNight)
                )
                setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.colorOnSecondaryNight)
                )
                setActionTextColor(
                    ContextCompat.getColor(requireContext(), R.color.colorOnSecondaryNight)
                )
                show()
            }
        }
    }
}