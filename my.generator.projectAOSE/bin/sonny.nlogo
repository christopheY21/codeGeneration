
	
	
to Citizen :
	if etat = "in_mall" [
		in_mall
]
	if etat = "selecting_destination" [
		selecting_destination
]
	if etat = "walking_in_mall" [
		walking_in_mall
]
	if etat = "waiting service" [
		waiting service
]
	if etat = "exiting mall" [
		exiting mall
]
	if etat = "executing_service" [
		executing_service
]
	if etat = "waiting_for_checkout" [
		waiting_for_checkout
]
	if etat = "checking_out" [
		checking_out
]
	if getCurrentTime  _=_time_to_go [
		teleport entrance 
		set etat "in_mall"
]

end
	


	
to start_node_citizen :

end
	


	
to in_mall :
	set etat "selecting_destination"

end
	


	
to end_node_exit :

end
	


	
to start_node_in_mal :

end
	


	
to selecting_destination :
	if true [
		setDestination  
		set etat "walking_in_mall"
]

end
	


	
to walking_in_mall :
	if  arrived_in_shop _and_ shop_open  [
		set etat "waiting service"
]
	if  nothing_to_do _and_ at_exit  [
		set etat "exiting mall"
]
	if shop_closed [
		set etat "selecting_destination"
]

end
	


	
	


	
to waiting service :
	if  next_in_line _and_ service_available  [
		setTaskDuration  
		set etat "executing_service"
]

end
	


	
to exiting mall :
	if true [
		exit
		set etat "node_end_mall"
]

end
	


	
to executing_service :
	if task_done [
		if rejected_proposal [
			set etat "selecting_destination"
]]
		if accepted_proposal [
			set etat "waiting_for_checkout"
]

end
	


	
to waiting_for_checkout :
	if  next_in_line _and_ checkout_available  [
		setCheckoutDuration  
		set etat "checking_out"
]

end
	


	
	


	
to checking_out :
	if checkout_done [
		updateNeededTasks  
		set etat "selecting_destination"
]

end
	


	
to node_end_mall :

end
	


to teleport [entrance] :
end
to getCurrentTime :
end
to setCheckoutDuration :
end
to updateNeededTasks :
end
to setTaskDuration :
end
to setDestination :
end

	