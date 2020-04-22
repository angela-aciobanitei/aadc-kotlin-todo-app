package com.ang.acb.todolearn.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.databinding.ItemTaskBinding


class TasksAdapter(private val viewModel: TasksViewModel)
    : ListAdapter<Task, TaskViewHolder>(TaskDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}


class TaskViewHolder private constructor(val binding: ItemTaskBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: TasksViewModel, item: Task) {
        binding.task = item
        binding.tasksViewModel = viewModel
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent:ViewGroup): TaskViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemTaskBinding.inflate(inflater, parent, false)

            return TaskViewHolder(binding)
        }
    }
}


class TaskDiff: DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }
}