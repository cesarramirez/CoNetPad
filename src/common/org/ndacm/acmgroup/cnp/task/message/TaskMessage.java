package org.ndacm.acmgroup.cnp.task.message;

import org.ndacm.acmgroup.cnp.task.message.TaskMessageFactory.TaskType;

/** 
 * A TaskMessage is an abstraction of the Tasks and their attributes. A
 * TaskMessage is basically a string replresentation of the task and is
 * used for sending and recieving them over the network.
 *         
 * @author Cesar
 * 
 */
public class TaskMessage {

	/**
	 * An Enum with the task type this message represents.
	 */
	private TaskType taskType;

	public static Character end = 4;
	public static Character delimiter = 5;

	/**
	 * An array of string that contain the fields required to rebuild a task.
	 */
	private String[] data;

	/**
	 * Overloaded constructor.
	 * 
	 * @param tasktype
	 *            Task type to represent
	 * @param data 
	 *            array of string representing their fields
	 */
	public TaskMessage(TaskType tasktype, String[] data) {
		this.taskType = tasktype;
		this.data = data;
	}

	/**
	 * Default constructor.
	 * 
	 * @param input
	 *            string received over the network that is going going to be translated
	 *            into a TaskMessage
	 */
	public TaskMessage(String input) {
		String[] elements = input.split(Character.toString(delimiter));

		int tasktypeId = Integer.parseInt(elements[0]);
		TaskType taskType = TaskType.values()[tasktypeId];

		String[] data = new String[elements.length - 1];
		for (int i = 0; i < data.length; i++) {
			data[i] = elements[1 + i];
		}

		this.taskType = taskType;
		this.data = data;
	}

	/**
	 * Get a message string for this TaskMessage.
	 * 
	 * @return Returns a string representation of this message. This string will
	 *         contain the task type and all the data.
	 */
	public String getMessageString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(taskType.ordinal());
		buffer.append(delimiter);
		for (String element : data) {
			buffer.append(element + delimiter);
		}
		buffer.append(end);

		return buffer.toString();
	}

	/**
	 * Get the task type for this message.
	 * 
	 * @return the task type
	 */
	public TaskType getTaskType() {
		return taskType;
	}

	/**
	 * Get the data array for this message.
	 * 
	 * @return the array that contains the set of data
	 */
	public String[] getData() {
		return data;
	}
}
