package com.android.fundamentals.workshop01

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.fundamentals.R
import androidx.recyclerview.widget.RecyclerView

class WS01ActorsFragment : Fragment() {
    private var recyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_actors, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_actors)
        recyclerView?.adapter=WS01ActorsAdapter()
    }

    override fun onStart() {
        super.onStart()

        updateData()
    }

    private fun updateData() {
        // TODO 05: Uncomment and fix "someRecycler" name if needed.
//        (someRecycler?.adapter as? WS01ActorsAdapter)?.apply {
//            bindActors(ActorsDataSource().getActors())
//        }

        // TODO 06: Open the WS01ActorsAdapter.kt file.
    }

    companion object {
        fun newInstance() = WS01ActorsFragment()
    }
}