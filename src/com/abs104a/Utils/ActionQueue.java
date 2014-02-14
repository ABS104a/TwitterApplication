package com.abs104a.Utils;

import java.util.LinkedList;


public class ActionQueue<T> extends LinkedList<T>{

	/**
	 *
	 */
	private static final long serialVersionUID = 4355961187764518314L;
	private final int TASKNUM;
	private Integer taskCount = 0;


	public ActionQueue(){
		TASKNUM = 10;
	}

	public ActionQueue(int taskMax){
		this.TASKNUM = taskMax;
	}

	public int getTaskNum(){
    	return taskCount;
    }

	public int addTaskNum(){
    	return ++taskCount;
    }

	public int minusTaskNum(){
    	return --taskCount;
    }

	public int getMaxTask(){
		return TASKNUM;
	}

	public void offerItem(T item){
		this.offer(item);
	}

	public T pollItem(){
		return this.poll();
	}

	public int getSize(){
		return this.size();
	}
}
