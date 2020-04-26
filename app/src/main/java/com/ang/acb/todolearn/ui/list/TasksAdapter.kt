package com.ang.acb.todolearn.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ang.acb.todolearn.data.local.Task
import com.ang.acb.todolearn.databinding.ItemTaskBinding


/**
 * A simple [PagedListAdapter] that binds [Task] items.
 *
 * [PagedListAdapter] is a RecyclerView.Adapter base class which can present
 * the content of [PagedList]s in a RecyclerView. It requests new pages as the
 * user scrolls, and handles new PagedLists by computing list differences on a
 * background thread, and dispatching minimal updates to the RecyclerView to
 * ensure minimal UI thread work.
 *
 * @see androidx.paging.PagedListAdapter
 * @see androidx.paging.AsyncPagedListDiffer
 */
class TasksAdapter(private val viewModel: TasksViewModel)
    : PagedListAdapter<Task, TaskViewHolder>(TaskDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(viewModel, getItem(position))
    }
}


/**
 * A simple [ViewHolder] that can bind a [Task] item. It also accepts
 * null items since the data may not have been fetched before it is bound.
 */
class TaskViewHolder private constructor(val binding: ItemTaskBinding)
    : RecyclerView.ViewHolder(binding.root) {

    /**
     * Items might be null if they are not paged in yet. PagedListAdapter
     * will re-bind the ViewHolder when Item is loaded.
     */
    fun bind(viewModel: TasksViewModel, item: Task?) {
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