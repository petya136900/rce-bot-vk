				var groupsArray = [];
				var groupsAllHash="none";
				var weekdays = ["monday","tuesday","wednesday","thursday","friday","saturday"];
				var checkMySqlInterval=undefined;
				var BS = {};
				function showContent(bool) {
					if(bool==undefined) {
						bool=false;
					}					
					var div = document.querySelector('#mainCenter');
					if(!bool) {
						div.classList.remove('hide');	
					} else {
						div.classList.add('hide');	
					}
					document.getElementById("createButt").disabled=bool;
					document.getElementById("removeButt").disabled=bool;
				}
				var MD5=function(d){d=unescape(encodeURIComponent(d));result=M(V(Y(X(d),8*d.length)));return result.toLowerCase()};function M(d){for(var _,m="0123456789ABCDEF",f="",r=0;r<d.length;r++)_=d.charCodeAt(r),f+=m.charAt(_>>>4&15)+m.charAt(15&_);return f}function X(d){for(var _=Array(d.length>>2),m=0;m<_.length;m++)_[m]=0;for(m=0;m<8*d.length;m+=8)_[m>>5]|=(255&d.charCodeAt(m/8))<<m%32;return _}function V(d){for(var _="",m=0;m<32*d.length;m+=8)_+=String.fromCharCode(d[m>>5]>>>m%32&255);return _}function Y(d,_){d[_>>5]|=128<<_%32,d[14+(_+64>>>9<<4)]=_;for(var m=1732584193,f=-271733879,r=-1732584194,i=271733878,n=0;n<d.length;n+=16){var h=m,t=f,g=r,e=i;f=md5_ii(f=md5_ii(f=md5_ii(f=md5_ii(f=md5_hh(f=md5_hh(f=md5_hh(f=md5_hh(f=md5_gg(f=md5_gg(f=md5_gg(f=md5_gg(f=md5_ff(f=md5_ff(f=md5_ff(f=md5_ff(f,r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+0],7,-680876936),f,r,d[n+1],12,-389564586),m,f,d[n+2],17,606105819),i,m,d[n+3],22,-1044525330),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+4],7,-176418897),f,r,d[n+5],12,1200080426),m,f,d[n+6],17,-1473231341),i,m,d[n+7],22,-45705983),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+8],7,1770035416),f,r,d[n+9],12,-1958414417),m,f,d[n+10],17,-42063),i,m,d[n+11],22,-1990404162),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+12],7,1804603682),f,r,d[n+13],12,-40341101),m,f,d[n+14],17,-1502002290),i,m,d[n+15],22,1236535329),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+1],5,-165796510),f,r,d[n+6],9,-1069501632),m,f,d[n+11],14,643717713),i,m,d[n+0],20,-373897302),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+5],5,-701558691),f,r,d[n+10],9,38016083),m,f,d[n+15],14,-660478335),i,m,d[n+4],20,-405537848),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+9],5,568446438),f,r,d[n+14],9,-1019803690),m,f,d[n+3],14,-187363961),i,m,d[n+8],20,1163531501),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+13],5,-1444681467),f,r,d[n+2],9,-51403784),m,f,d[n+7],14,1735328473),i,m,d[n+12],20,-1926607734),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+5],4,-378558),f,r,d[n+8],11,-2022574463),m,f,d[n+11],16,1839030562),i,m,d[n+14],23,-35309556),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+1],4,-1530992060),f,r,d[n+4],11,1272893353),m,f,d[n+7],16,-155497632),i,m,d[n+10],23,-1094730640),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+13],4,681279174),f,r,d[n+0],11,-358537222),m,f,d[n+3],16,-722521979),i,m,d[n+6],23,76029189),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+9],4,-640364487),f,r,d[n+12],11,-421815835),m,f,d[n+15],16,530742520),i,m,d[n+2],23,-995338651),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+0],6,-198630844),f,r,d[n+7],10,1126891415),m,f,d[n+14],15,-1416354905),i,m,d[n+5],21,-57434055),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+12],6,1700485571),f,r,d[n+3],10,-1894986606),m,f,d[n+10],15,-1051523),i,m,d[n+1],21,-2054922799),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+8],6,1873313359),f,r,d[n+15],10,-30611744),m,f,d[n+6],15,-1560198380),i,m,d[n+13],21,1309151649),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+4],6,-145523070),f,r,d[n+11],10,-1120210379),m,f,d[n+2],15,718787259),i,m,d[n+9],21,-343485551),m=safe_add(m,h),f=safe_add(f,t),r=safe_add(r,g),i=safe_add(i,e)}return Array(m,f,r,i)}function md5_cmn(d,_,m,f,r,i){return safe_add(bit_rol(safe_add(safe_add(_,d),safe_add(f,i)),r),m)}function md5_ff(d,_,m,f,r,i,n){return md5_cmn(_&m|~_&f,d,_,r,i,n)}function md5_gg(d,_,m,f,r,i,n){return md5_cmn(_&f|m&~f,d,_,r,i,n)}function md5_hh(d,_,m,f,r,i,n){return md5_cmn(_^m^f,d,_,r,i,n)}function md5_ii(d,_,m,f,r,i,n){return md5_cmn(m^(_|~f),d,_,r,i,n)}function safe_add(d,_){var m=(65535&d)+(65535&_);return(d>>16)+(_>>16)+(m>>16)<<16|65535&m}function bit_rol(d,_){return d<<_|d>>>32-_}
				function disableLoader(bool) {
					if(bool==undefined) {
						bool=false;
					}
					var onLoadAnim = document.getElementById("onLoadAnim");
					var main = document.getElementById("main");
					if(!bool) {
						onLoadAnim.classList.add("hide");									
						main.classList.add("hide");			
					} else {
						onLoadAnim.classList.remove("hide");									
						main.classList.remove("hide");								
						document.getElementById("loadingText").textContent="Загрузка...";
						document.getElementById("loadingText").style.color="steelblue";						
					}					
				}				
				function printError(message) {
					disableLoader(true);
					showContent(true);
					document.getElementById("loadingText").textContent=message;
					document.getElementById("loadingText").style.color="RED";
					document.getElementById("onLoadAnim").classList.remove("hide");
				}
				var needUpdateBs = false;
				function updateBotSettings() {
					// remove old settings
					var oldSettings = document.getElementsByClassName("setting");
					for(var i=oldSettings.length;i>0;i--) {
						oldSettings[(i-1)].remove();
					}
					var settingsList = document.getElementById("settingsList");
					for(setting in BS) {
						var pSetting = document.createElement("p");
						pSetting.classList.add("setting");
						var spanVarName = document.createElement("span");
						spanVarName.classList.add("varName");
						spanVarName.textContent=setting;
						var spanVarType = document.createElement("span");
						spanVarType.classList.add("varType");
						var inputValue = document.createElement("input");
						var type = typeof(BS[setting]);
						//console.log(type);
						if(type=="string") {
							spanVarType.textContent="(String)";
							inputValue.value=BS[setting];
							inputValue = document.createElement("textarea");
							inputValue.value=BS[setting];
						} else if(type=="integer") {
							spanVarType.textContent="(Integer)";
							inputValue.value=BS[setting];
						} else if(type=="boolean") {
							spanVarType.textContent="(Boolean)";
							inputValue.setAttribute("type","checkbox");
							if(BS[setting]) {
								inputValue.checked=true;
							} else {
								inputValue.checked=false;
							}
						}
						pSetting.appendChild(spanVarName);
						pSetting.appendChild(spanVarType);
						pSetting.appendChild(inputValue);
						settingsList.appendChild(pSetting);
					}
				}
				function setNewBotSettings() {
					//////////////////////////////////////////////////////////////////
					var settings = document.getElementById("settingsList").getElementsByClassName("setting")
					var settingsArray = Array.from(settings)
					var settingsJsonString = "{"
					var firstTime=true;
					settingsArray.forEach(function(setting) {
						var varName = setting.getElementsByClassName("varName")[0].textContent;
						var varType = setting.getElementsByClassName("varType")[0].textContent.replace("(","").replace(")","");
						var varValue;
						if(varType=="Boolean") {
							varValue = setting.getElementsByTagName("input")[0].checked;
						} else if(varType=="Integer") { 
							varValue = setting.getElementsByTagName("input")[0].value;
						} else {
							varValue = JSON.stringify(setting.getElementsByTagName("textarea")[0].value);
						}
						//console.log("("+varType+") "+varName+": "+varValue);
						if(firstTime) {
							firstTime=false;
						} else {
							settingsJsonString = settingsJsonString.concat(",");
						}
						settingsJsonString = settingsJsonString.concat("\""+varName+"\":"+varValue);
					});
					settingsJsonString = settingsJsonString.concat("}");
					var settingsObj = JSON.parse(settingsJsonString);
					BS=settingsObj;
					console.log(BS);
					//for(setting in settingsObj) {
					//	console.log(setting);
					//	console.log(settingsObj[setting]);
					//}
					needUpdateBs=true;
					//////////////////////////////////////////////////////////////////
				}
				function sqlLoadBS() {
					var request = new XMLHttpRequest();
					var url = "sql.php?type=bs";
					request.open('GET', url);
					request.setRequestHeader('Content-Type', 'application/x-www-form-url');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							var result = JSON.parse(request.responseText);
							BS = result.response;
							if(BS.replace_mainshelude) { if(App.state) {App.toggleIt(); } }
							document.getElementsByClassName( "containerSld" )[0].addEventListener('click', function() {
								//console.log(App.state)
								BS.replace_mainshelude=!(BS.replace_mainshelude);
							});		
							document.getElementById("settingsPanel").style.display = "block";
							updateBotSettings();
							document.getElementById("addSettings").addEventListener("click", function() {
								document.getElementById("botSettings").style.display = "block";
							});		
							document.getElementById("closeSettings").addEventListener("click", function() {
								document.getElementById("botSettings").style.display = "none";
							});
							document.getElementById("saveSettings").addEventListener("click", function() {
								setNewBotSettings();
								document.getElementById("botSettings").style.display = "none";
							});							
							document.getElementById("cancelSettings").addEventListener("click", function() {
								updateBotSettings();
								document.getElementById("botSettings").style.display = "none";
							});								
						}
					});
					request.send();						
				}
				function sqlLoadGroups() {
					var request = new XMLHttpRequest();
					var url = "sql.php?type=load";
					request.open('GET', url);
					request.setRequestHeader('Content-Type', 'application/x-www-form-url');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							disableLoader();
							showContent();
							//console.log(request.responseText);							
							var result = JSON.parse(request.responseText);
							if(result.error=="true") {
								printError("MySql сервер не запущен");
								if(!(checkMySqlInterval==undefined)) {
									clearInterval(checkMySqlInterval);
								}
								checkMySqlInterval = setInterval(sqlLoadGroups, 7000);
							} else {
								if(!(checkMySqlInterval==undefined)) {
									clearInterval(checkMySqlInterval);
								}
								sqlLoadBS();
								groupsAllHash=result.groupsHash;
								result.response.forEach(function(sqlGroup){
									//console.log(sqlGroup);
									createBlock(
										sqlGroup.groupName,
										sqlGroup.pairs,
										sqlGroup.weekDay,
										false,
										sqlGroup.hash
									);
								});
								runUpdates();
							}
						} else if(request.readyState === 4) {
							console.log("ERROR: "+request.responseText);
							printError("Бот не запущен");
							if(checkMySqlInterval==undefined) {
								checkMySqlInterval = setInterval(sqlLoadGroups, 7000);
							}
						}
					});
					request.send();					
				}
				var ucStarted=false;
				function runUpdates() {
					if(ucStarted==false) {
						ucStarted=true;
						checkForUpdateGroups();
					} else {
						console.log("Already uc..");
					}
				}
				async function checkForUpdateGroups() {
					if(ucStarted) {
						return sleep(1000).then(() => { 
							if(ucStarted) {
								//checkForUpdateGroups();
								//console.log("Update..");
								var request = new XMLHttpRequest();
								var url = "sql.php?type=grouphash";
								request.open('GET', url);
								request.setRequestHeader('Content-Type', 'application/x-www-form-url');
								request.addEventListener("readystatechange", () => {
									if (request.readyState === 4 && request.status === 200) {
										//console.log(request.responseText);							
										var result = JSON.parse(request.responseText);
										if(result.error=="true") {
											// error true
										} else {
											if(result.groupsHash==groupsAllHash) {
												//console.log("sync: ok");
												checkForUpdateGroups();
											} else {
												console.log("sync: need update");
												ucStarted=false;
												sqlLoadGroups();
											}
										}
									} else if(request.readyState === 4) {
										console.log("HashUpdate error..");
										checkForUpdateGroups();
									}
								});
								request.send();									
							};
							return 1;
						});
					}
				}
				function sleep(ms) {
					return new Promise(resolve => setTimeout(resolve, ms));
				}
				function writeGroups() {
					var password = document.getElementById("passwordInp");
					var printButton = document.getElementById("sumButton");
					if(!(password.value.trim().length>0)) {
						alert("Введите пароль");
						return false;
					}
					printButton.disabled=true;
					printButton.value="Ожидание..";
					var groupsForSend = [];
					weekdays.forEach(function(weekDay) {
						//console.log(weekDay);
						var groupsWeek = Array.from(document.getElementById(weekDay).getElementsByClassName("group"));
						groupsWeek.forEach(function(oneGroup) {
							//console.log(oneGroup);
							var groupName = oneGroup.getElementsByClassName("groupName")[0].value.trim();
							var pairs = [];
							var pairsArray = Array.from(oneGroup.getElementsByClassName("partWeek"));
							pairsArray.forEach(function(pairLine) {
								if(!((pairLine.getAttribute("pairnum"))==undefined)) {
									if(pairLine.getElementsByClassName("pairName")[0].value.trim().length>0) {
										//console.log(pairLine);
										pairs.push({
												 pairNum: Number(pairLine.getAttribute("pairnum"))
												,pairName: pairLine.getElementsByClassName("pairName")[0].value.replace("\\","/")
												,pairCab: pairLine.getElementsByClassName("pairCab")[0].value.replace("\\","/")
											});
									}
								}
							});
							groupsForSend.push({
								groupName: groupName,
								weekDay: weekDay,
								pairs: pairs
							});
						});
					});
					var sqlRequest={
						password:MD5(password.value.trim()),
						groups:groupsForSend
					};
					//console.log(JSON.stringify(sqlRequest));
						var request = new XMLHttpRequest();
						var url = "sql.php?type=send";
						var nonError=false;
						request.open('POST', url);
						request.setRequestHeader('Content-Type', 'application/json');
						request.addEventListener("readystatechange", () => {
							if (request.readyState === 4 && request.status === 200) {
							console.log(request.responseText);
							  var result = JSON.parse(request.responseText);
							  if(result.error=="true") {
							    if(result.desc=="badpasswd") {
									alert("Неправильный пароль");
								} else {
									printError("MySql: ошибка");
									if(!(checkMySqlInterval==undefined)) {
										clearInterval(checkMySqlInterval);
									}
									checkMySqlInterval = setInterval(sqlLoadGroups, 7000);									
								}
							  } else if(result.error=="false") {
									nonError=true;
									if(!needUpdateBs) {
										sqlLoadGroups();removeBlock("*",false);
										alert("Обновлено!");
									}
							  } else {
								printError("Бот не запущен");
								if(!(checkMySqlInterval==undefined)) {
									clearInterval(checkMySqlInterval);
								}
								checkMySqlInterval = setInterval(sqlLoadGroups, 7000);																	
							  }
								if(needUpdateBs&nonError) {
									//console.log("Нужно обновить BS");
									needUpdateBs=false;
									updateBS();
								} else {
									printButton.disabled=false;
									printButton.value="Перезаписать";
									//console.log('Убираю заглушку');
								}							  
							}
						});
						request.send(JSON.stringify(sqlRequest));
				}
				function updateBS() {
					var printButton = document.getElementById("sumButton");
					var password = document.getElementById("passwordInp");
					var sqlRequest={
						password:MD5(password.value.trim()),
						botSettings:BS
					};
					//console.log(JSON.stringify(sqlRequest));
						var request = new XMLHttpRequest();
						var url = "sql.php?type=gbs";
						var nonError=false;
						request.open('POST', url);
						request.setRequestHeader('Content-Type', 'application/json');
						request.addEventListener("readystatechange", () => {
							if (request.readyState === 4 && request.status === 200) {
							console.log(request.responseText);
							  var result = JSON.parse(request.responseText);
							  if(result.error=="true") {
									alert("something went wrong, CODE#1");							
							  } else if(result.error=="false") {
									alert("Обновлено!");
							  } else {
									alert("something went wrong, CODE#2");							
							  }
							}
							printButton.disabled=false;
							printButton.value="Перезаписать";
						});
						request.send(JSON.stringify(sqlRequest));					
				}
				function removeBlock(groupName,cnfrm,weekDay) {
					if(groupName==undefined) {
						groupName=document.getElementById("groupForDelete").value;
						if(groupName==undefined) {
							if(cnfrm) {
								alert("Введите имя группы");
							}
							return;						
						} else {
							if(groupName+"".trim().length<1) {
								if(cnfrm) {
									alert("Введите имя группы");
								}
								return;									
							}
						}
					}	
					var groupsForDelete=[];
					if(groupName=="*") {
						groupsForDelete = document.getElementsByClassName("group");
					} else {
						if(weekDay==undefined) {
							groupsForDelete = document.getElementsByClassName("group-"+groupName);
						} else {
							groupsForDelete = document.getElementById(weekDay).getElementsByClassName("group-"+groupName);
						}
					}
					if(groupsForDelete!=undefined) {
						if(groupsForDelete.length<1) {
							if(cnfrm) {
								if(weekDay==undefined) {
									alert("Группа "+groupName+" не найдена");
								}
							}
						} else {
							if(cnfrm) {
								var confText = "Вы точно хотите удалить группу "+groupName+"?";
								if(groupName=="*") {
									confText = "Вы точно хотите удалить все группы?";
								}
								if(!confirm(confText)) {
									return;
								}
							}
							//console.log(groupsForDelete.length+" group");
							for(i=groupsForDelete.length;i>0;--i) {
								//console.log("Удаляю "+groupsForDelete[i-1]);
								groupsForDelete[i-1].remove();
							}
							var wd = document.getElementById(weekDay);
							var grs;
							if(wd==undefined) {
								grs=0;
							} else {
								grs = document.getElementById(weekDay).getElementsByClassName("group-"+groupName);
							}
							if((wd==undefined)|(grs==0)) {
								grIndex = groupsArray.indexOf(groupName);
								if (grIndex !== -1) groupsArray.splice(grIndex, 1);								
							} 
						}
					} else {
						alert("undef");
					}
				}
				function beautifyGroupName(gname) {
					var jsonRequest={
						groupName:gname
					};
					//console.log(JSON.stringify(sqlRequest));
					var request = new XMLHttpRequest();
					var url = "sql.php?type=bgn";
					var nonError=false;
					request.open('POST', url, false);
					request.setRequestHeader('Content-Type', 'application/json');
					request.send(JSON.stringify(jsonRequest));
					if (request.readyState === 4 && request.status === 200) {
						return request.responseText;
					}
				}
				function createBlock(groupName, pairsArr, weekDay, cnfrm, hash) {
					/*
					groupName="ССА-301";		
					pairs = [
						{pairNum: 0,pairName: "Комп Сети",pairCab: "301"},
						{pairNum: 1,pairName: "Пара какая-т",pairCab: "222"},
						{pairNum: 2,pairName: "Hello World",pairCab: "404"}
					]					
					*/
					var pairs = ["","","","","","","",""]
					var cabs = ["","","","","","","",""]
					if(pairsArr!=undefined) {
						pairsArr.forEach(function(pair){
							pairs[pair.pairNum]=pair.pairName;
							cabs[pair.pairNum]=pair.pairCab;
						});
					}
					if(groupName==undefined) {
						groupName=document.getElementById("groupForAdd").value;
						if(groupName==undefined) {
							if(cnfrm) {
								alert("Введите имя группы");
							}
							return;						
						} else {
							if(groupName+"".trim().length<1) {
								if(cnfrm) {
									alert("Введите имя группы");
								}
								return;									
							}
						}
						groupName=beautifyGroupName(groupName);
					}
					var notPush=false;
					for(gc=0;gc<groupsArray.length;gc++){
						if(groupsArray[gc]==groupName) {
							notPush=true;
							if(weekDay==undefined) {
								if(cnfrm) {
									alert("Группа "+groupName+" уже добавлена");
								}
								return;
							} else {
								var wd = document.getElementById(weekDay);
								var grs = wd.getElementsByClassName("group-"+groupName);
								for(i=grs.length;i>0;--i) {
									//console.log("Удаляю "+grs[i-1]);
									grs[i-1].remove();
								}								
							}
						}						
					}
					if(cnfrm) {
						if(!confirm("Добавить группу "+groupName+"?")) {
							return;
						}			
					}		
					if(!notPush) {
						groupsArray.push(groupName.trim());
					}
					for(week=0;week<weekdays.length;week++){
						if(weekDay!=undefined) {
							if(weekDay!=weekdays[week]) {
								continue;
							}
						}						
						var group = document.createElement("div");
							group.classList.add("group");
							group.classList.add("group-"+groupName);
						var inputHash = document.createElement("input");
							inputHash.setAttribute("type","hidden");
							inputHash.setAttribute("value",hash);
							inputHash.classList.add("hash");
						group.appendChild(inputHash);
						var groupNameLine = document.createElement("p");
							groupNameLine.classList.add("partWeek");
							groupNameLine.textContent="Группа: ";
						var inputGroupName = document.createElement("input");
							inputGroupName.setAttribute("type","text");
							inputGroupName.setAttribute("size","8");
							inputGroupName.setAttribute("maxlength","9");
							inputGroupName.setAttribute("value",groupName);
							inputGroupName.classList.add("groupField");
							inputGroupName.classList.add("groupName");
							inputGroupName.disabled=true;
						var spanCab = document.createElement("span");
							spanCab.classList.add("kabField");
							spanCab.textContent="Каб.";
						groupNameLine.appendChild(inputGroupName);
						groupNameLine.appendChild(spanCab);
						group.appendChild(groupNameLine);
						var counter = 0;
						for(var i=0;i<8;i++) {
							pairLine = document.createElement("p");
								pairLine.classList.add("partWeek");
								pairLine.setAttribute("pairNum",counter);
								pairLine.textContent=counter+" Пара: ";
							inputPair = document.createElement("input");
								inputPair.setAttribute("type","text");
								inputPair.setAttribute("size","15");
								inputPair.setAttribute("maxlength","50");
								inputPair.setAttribute("value",pairs[i]);
								inputPair.classList.add("pairName");
							pairLine.appendChild(inputPair);
							pairLine.append(" | ");
							inputCab = document.createElement("input");
								inputCab.setAttribute("type","text");
								inputCab.setAttribute("size","3");
								inputCab.setAttribute("maxlength","15");
								inputCab.setAttribute("value",cabs[i]);
								inputCab.classList.add("pairCab");
							pairLine.appendChild(inputCab);
							group.appendChild(pairLine);
							counter++;					
						}						
						document.getElementById(weekdays[week]).appendChild(group);
					}
					/*
					pairs.forEach(function(pair){
						console.log(pair.pairName+" | "+pair.cabName);
						pairLine = document.createElement("p");
							pairLine.classList.add("partWeek");
							pairLine.textContent=counter+" Пара: ";
						inputPair = document.createElement("input");
							inputPair.setAttribute("type","text");
							inputPair.setAttribute("size","15");
							inputPair.setAttribute("maxlength","50");
							inputPair.setAttribute("value",pair.pairName);
						pairLine.appendChild(inputPair);
						pairLine.append(" | ");
						inputCab = document.createElement("input");
							inputCab.setAttribute("type","text");
							inputCab.setAttribute("size","1");
							inputCab.setAttribute("maxlength","15");
							inputCab.setAttribute("value",pair.cabName);
						pairLine.appendChild(inputCab);
						group.appendChild(pairLine);
						counter++;
					});
					*/			
				}
				 window.onload = function() {
					var inputGroup = document.getElementById("inputUserID");
					inputGroup.oninput = function() {
						//console.log(inputGroup.value);
						var groups = Array.from(document.getElementsByClassName("group"));					
						groups.forEach(function(group) {
							if(((group.classList[1].toString().indexOf(inputGroup.value))==-1)) {
								group.classList.add("hide");
							} else {
								group.classList.remove("hide");
							}
						});		
					};					 
					sqlLoadGroups();
				};
