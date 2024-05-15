package com.example.todoapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentTodoListBinding
import com.example.todoapp.viewmodel.ListTodoViewModel

class TodoListFragment : Fragment() {
    private lateinit var binding: FragmentTodoListBinding
    private val todoListAdapter = TodoListAdapter(arrayListOf()) { todo ->
        // Handle click event
    }
    private lateinit var viewModel: ListTodoViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(ListTodoViewModel::class.java)
        viewModel.refresh()

        binding.recViewTodo.layoutManager = LinearLayoutManager(context)
        binding.recViewTodo.adapter = todoListAdapter

        binding.btnFab.setOnClickListener {
            val action = TodoListFragmentDirections.actionCreateTodo()
            Navigation.findNavController(it).navigate(action)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.todoLD.observe(viewLifecycleOwner, Observer {
            todoListAdapter.updateTodoList(it)
            if (it.isEmpty()) {
                binding.recViewTodo.visibility = View.GONE
                binding.txtError.visibility = View.VISIBLE
                binding.txtError.text = "Your todo list is empty."
            } else {
                binding.recViewTodo.visibility = View.VISIBLE
                binding.txtError.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressLoad.visibility = View.VISIBLE
                binding.recViewTodo.visibility = View.GONE
                binding.txtError.visibility = View.GONE
            } else {
                binding.progressLoad.visibility = View.GONE
            }
        })

        viewModel.todoLoadErrorLD.observe(viewLifecycleOwner, Observer { isError ->
            if (isError) {
                binding.txtError.visibility = View.VISIBLE
                binding.txtError.text = "An error occurred while loading data."
            } else {
                binding.txtError.visibility = View.GONE
            }
        })
    }
}