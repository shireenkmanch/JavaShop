/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function submitForm(command) {
	
	document.getElementById("command").value = command;
	document.forms[0].submit();
	
}


function submitFormWithAction(action, command) {
	
	document.forms[0].action = action;
	document.getElementById("command").value = command;
	document.forms[0].submit();
	
}

