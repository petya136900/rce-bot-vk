				var teachersArray = [];
				var teachersAllHash="none";
				var pairsAllHash="none";
				var pairsAllArray=[];
				var checkMySqlInterval=undefined;
				var checkMySqlIntervalPairs=undefined;
				function showContent(bool) {
					if(bool==undefined) {
						bool=false;
					}					
					bool=!bool;
					var div = document.querySelector('#mainCenter');
					var div2 = document.querySelector('#panel');
					if(!bool) {
						div.classList.remove('hide');
						div2.classList.remove('hide');
					} else {
						div.classList.add('hide');	
						div2.classList.add('hide');	
					}
				}
				var MD5=function(d){d=unescape(encodeURIComponent(d));result=M(V(Y(X(d),8*d.length)));return result.toLowerCase()};function M(d){for(var _,m="0123456789ABCDEF",f="",r=0;r<d.length;r++)_=d.charCodeAt(r),f+=m.charAt(_>>>4&15)+m.charAt(15&_);return f}function X(d){for(var _=Array(d.length>>2),m=0;m<_.length;m++)_[m]=0;for(m=0;m<8*d.length;m+=8)_[m>>5]|=(255&d.charCodeAt(m/8))<<m%32;return _}function V(d){for(var _="",m=0;m<32*d.length;m+=8)_+=String.fromCharCode(d[m>>5]>>>m%32&255);return _}function Y(d,_){d[_>>5]|=128<<_%32,d[14+(_+64>>>9<<4)]=_;for(var m=1732584193,f=-271733879,r=-1732584194,i=271733878,n=0;n<d.length;n+=16){var h=m,t=f,g=r,e=i;f=md5_ii(f=md5_ii(f=md5_ii(f=md5_ii(f=md5_hh(f=md5_hh(f=md5_hh(f=md5_hh(f=md5_gg(f=md5_gg(f=md5_gg(f=md5_gg(f=md5_ff(f=md5_ff(f=md5_ff(f=md5_ff(f,r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+0],7,-680876936),f,r,d[n+1],12,-389564586),m,f,d[n+2],17,606105819),i,m,d[n+3],22,-1044525330),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+4],7,-176418897),f,r,d[n+5],12,1200080426),m,f,d[n+6],17,-1473231341),i,m,d[n+7],22,-45705983),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+8],7,1770035416),f,r,d[n+9],12,-1958414417),m,f,d[n+10],17,-42063),i,m,d[n+11],22,-1990404162),r=md5_ff(r,i=md5_ff(i,m=md5_ff(m,f,r,i,d[n+12],7,1804603682),f,r,d[n+13],12,-40341101),m,f,d[n+14],17,-1502002290),i,m,d[n+15],22,1236535329),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+1],5,-165796510),f,r,d[n+6],9,-1069501632),m,f,d[n+11],14,643717713),i,m,d[n+0],20,-373897302),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+5],5,-701558691),f,r,d[n+10],9,38016083),m,f,d[n+15],14,-660478335),i,m,d[n+4],20,-405537848),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+9],5,568446438),f,r,d[n+14],9,-1019803690),m,f,d[n+3],14,-187363961),i,m,d[n+8],20,1163531501),r=md5_gg(r,i=md5_gg(i,m=md5_gg(m,f,r,i,d[n+13],5,-1444681467),f,r,d[n+2],9,-51403784),m,f,d[n+7],14,1735328473),i,m,d[n+12],20,-1926607734),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+5],4,-378558),f,r,d[n+8],11,-2022574463),m,f,d[n+11],16,1839030562),i,m,d[n+14],23,-35309556),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+1],4,-1530992060),f,r,d[n+4],11,1272893353),m,f,d[n+7],16,-155497632),i,m,d[n+10],23,-1094730640),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+13],4,681279174),f,r,d[n+0],11,-358537222),m,f,d[n+3],16,-722521979),i,m,d[n+6],23,76029189),r=md5_hh(r,i=md5_hh(i,m=md5_hh(m,f,r,i,d[n+9],4,-640364487),f,r,d[n+12],11,-421815835),m,f,d[n+15],16,530742520),i,m,d[n+2],23,-995338651),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+0],6,-198630844),f,r,d[n+7],10,1126891415),m,f,d[n+14],15,-1416354905),i,m,d[n+5],21,-57434055),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+12],6,1700485571),f,r,d[n+3],10,-1894986606),m,f,d[n+10],15,-1051523),i,m,d[n+1],21,-2054922799),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+8],6,1873313359),f,r,d[n+15],10,-30611744),m,f,d[n+6],15,-1560198380),i,m,d[n+13],21,1309151649),r=md5_ii(r,i=md5_ii(i,m=md5_ii(m,f,r,i,d[n+4],6,-145523070),f,r,d[n+11],10,-1120210379),m,f,d[n+2],15,718787259),i,m,d[n+9],21,-343485551),m=safe_add(m,h),f=safe_add(f,t),r=safe_add(r,g),i=safe_add(i,e)}return Array(m,f,r,i)}function md5_cmn(d,_,m,f,r,i){return safe_add(bit_rol(safe_add(safe_add(_,d),safe_add(f,i)),r),m)}function md5_ff(d,_,m,f,r,i,n){return md5_cmn(_&m|~_&f,d,_,r,i,n)}function md5_gg(d,_,m,f,r,i,n){return md5_cmn(_&f|m&~f,d,_,r,i,n)}function md5_hh(d,_,m,f,r,i,n){return md5_cmn(_^m^f,d,_,r,i,n)}function md5_ii(d,_,m,f,r,i,n){return md5_cmn(m^(_|~f),d,_,r,i,n)}function safe_add(d,_){var m=(65535&d)+(65535&_);return(d>>16)+(_>>16)+(m>>16)<<16|65535&m}function bit_rol(d,_){return d<<_|d>>>32-_}
				function disableLoader(bool) {
					if(bool==undefined) {
						bool=false;
					}
					bool=!bool;
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
					note({
						content: message,
						type: "error",
						time: 3
					});					
					teachersAllHash="none";
					disableLoader(false);
					showContent(false);
					document.getElementById("loadingText").textContent=message;
					document.getElementById("loadingText").style.color="RED";
					document.getElementById("onLoadAnim").classList.remove("hide");
				}
				function printInfo(message) {
					disableLoader(false);
					showContent(false);
					document.getElementById("loadingText").textContent=message;
					document.getElementById("loadingText").style.color="#42648b";
					document.getElementById("onLoadAnim").classList.remove("hide");
				}
				function closeInfo() {
					disableLoader(true);
					showContent(true);
				}
				var firstRequest=true; // reset after succesfuel GET
				function sqlLoadTeachers() {
					removeAllTeachers();
					removeAllTeachersOptions();
					var request = new XMLHttpRequest();
					var url = "sql.php?type=getteachers";
					request.open('GET', url);
					request.setRequestHeader('Content-Type', 'application/x-www-form-url');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							disableLoader(true);
							showContent(true);
							//console.log(request.responseText);							
							var result = JSON.parse(request.responseText);
							console.log(result);
							if(result.error=="true") {
								printError("MySql сервер не запущен");
								if(!(checkMySqlInterval==undefined)) {
									clearInterval(checkMySqlInterval);
								}
								checkMySqlInterval = setInterval(sqlLoadTeachers, 7000);
							} else {
								if(!(checkMySqlInterval==undefined)) {
									clearInterval(checkMySqlInterval);
								}
								firstRequest=true;
								teachersAllHash=result.hash;
								var finded=false;
								result.teachers.forEach(function(teacher){
									if(teacherShows) {
										if(tempTeacherId==teacher.id) {
											finded=true;
											console.log("Найден");
											updateTeacherInfo(teacher.fullName);
										}
									}
									addTeacher(teacher.id,teacher.fullName);
								});
								if(!finded) {
									cancelTeacherInfo();
								}
								runUpdates();
							}
						} else if(request.readyState === 4) {
							console.log("ERROR: "+request.responseText);
							printError("Бот не запущен");
							if(checkMySqlInterval==undefined) {
								checkMySqlInterval = setInterval(sqlLoadTeachers, 7000);
							}
						}
					});
					if(firstRequest) {
						firstRequest=false;
						printInfo("Получение списка преподавателей..");
					}
					request.send();					
				}
				function updateTeacherInfo(name,ptid) {
					document.getElementById("fullNameInput").value=name;
					selectTeacherOption(ptid);	
				}
				function sqlLoadPairs() {
					var request = new XMLHttpRequest();
					var url = "sql.php?type=getpairs";
					request.open('GET', url);
					request.setRequestHeader('Content-Type', 'application/x-www-form-url');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							//console.log(request.responseText);							
							var result = JSON.parse(request.responseText);
							if(result.error=="true") {
								printError("MySql сервер не запущен");
								if(!(checkMySqlIntervalPairs==undefined)) {
									clearInterval(checkMySqlIntervalPairs);
								}
								checkMySqlIntervalPairs = setInterval(sqlLoadPairs, 7000);
							} else {
								if(!(checkMySqlIntervalPairs==undefined)) {
									clearInterval(checkMySqlIntervalPairs);
								}
								pairsAllHash=result.hash;
								pairsAllArray=result.pairs;
								//console.log(pairsAllArray);
								updatePairsDom(result.pairs)
								runUpdatesPairs();
							}
						} else if(request.readyState === 4) {
							console.log("ERROR: "+request.responseText);
							note({
								content: "Не удалось получить список пар",
								type: "error",
								time: 3
							});	
							if(checkMySqlIntervalPairs==undefined) {
								checkMySqlIntervalPairs = setInterval(sqlLoadPairs, 7000);
							}
						}
					});
					request.send();					
				}
				function updatePairsDom(pairs) {
					var teacherShowWindow = document.getElementById("teacherShowWindow");
					if(teacherShowWindow.style.display!=undefined) {
						if(teacherShowWindow.style.display!="none") {
							if(teacherShowWindow.style.display.trim().length>0) {
								loadTeacherPairs(document.getElementById("teacherIdInput").value);
							}
						}
					}
					var finded=false;
					pairs.forEach(function(pair){
						if(pairShows) {
							if(tempPairId==pair.id) {
								finded=true;
								updatePairInfo(pair.name,pair.teacherID);
							}
						}
						addUnknownPair(pair.id,pair.name,pair.teacherID);
					});
					if(!finded) {
						cancelPairInfo();
					}
					removeAllPairsMarks();
				}
				function updatePairInfo(name,teacherID) {
					document.getElementById("pairNameInput").value=name;
					
				}
				function removeAllPairsMarks() {
					var unkDivDom = document.getElementById("unkDiv");
					var unkpairs = unkDivDom.getElementsByClassName("unkPair");
					for(var i=(unkpairs.length-1);i>=0;i--) {
						if(i<0) {
							return;
						}
						var updated = unkpairs[i].getAttribute("updated");
						if(updated!="true") {
							unkpairs[i].remove();
						} else {
							unkpairs[i].removeAttribute("updated");
						}
					}
				}
				function addUnknownPair(pid,name,tid) {
					if(tid==undefined) {
						tid=0;
					} else if(tid!=0) {
						return;
					}
					var unkDivDom = document.getElementById("unkDiv");
					var unkpairs = unkDivDom.getElementsByClassName("unkPair");
					var oldPair=false;
					for(unkpair of unkpairs) {
						var oPid = unkpair.getAttribute("pid");
						if(oPid==pid) {
							oldPair=true;
							break;
						} else {
							//
						}
					}
					if(oldPair) {
						unkpair.textContent=name;
						unkpair.setAttribute("updated","true");
						return;
					}
					var pPair = document.createElement("p");
					pPair.setAttribute("pid",pid);
					pPair.setAttribute("onclick","showPair(this)");
					pPair.setAttribute("class","unkPair");
					pPair.setAttribute("updated","true");
					pPair.textContent=name;
					unkDivDom.appendChild(pPair);
				}
				var ucStarted=false;
				function runUpdates() {
					if(ucStarted==false) {
						ucStarted=true;
						checkForUpdateTeachers();
					} else {
						console.log("Already uc..");
					}
				}
				var ucStartedPairs=false;
				function runUpdatesPairs() {
					if(ucStartedPairs==false) {
						ucStartedPairs=true;
						checkForUpdatePairs();
					} else {
						console.log("Already uc2..");
					}
				}	
				var afterChange2=false;
				async function checkForUpdatePairs() {
					if(ucStartedPairs) {
						return sleep(1700).then(() => { 
							if(ucStartedPairs) {
								//console.log("Update..");
								var request = new XMLHttpRequest();
								var url = "sql.php?type=pairshash";
								request.open('GET', url);
								request.setRequestHeader('Content-Type', 'application/x-www-form-url');
								request.addEventListener("readystatechange", () => {
									if (request.readyState === 4 && request.status === 200) {
										console.log(request.responseText);							
										var result = JSON.parse(request.responseText);
										if(result.error=="true") {
											
										} else {
											if(result.hash==pairsAllHash) {
												//console.log("sync: ok");
												checkForUpdatePairs();
											} else {
												console.log("sync: need pairs update");
												if(!afterChange2) {
													note({
														content: "Синхронизирован(Пары)",
														type: "info",
														time: 3
													});					
												}	
												afterChange2=false;
												ucStartedPairs=false;
												sqlLoadPairs();
											}
										}
									} else if(request.readyState === 4) {
										pairsAllHash="none";
										closePairInfo();
										checkForUpdatePairs();
									}
								});
								request.send();									
							};
							return 1;
						});
					}
				}				
				var afterChange=false;
				async function checkForUpdateTeachers() {
					if(ucStarted) {
						return sleep(2000).then(() => { 
							if(ucStarted) {
								//console.log("Update..");
								var request = new XMLHttpRequest();
								var url = "sql.php?type=teachershash";
								request.open('GET', url);
								request.setRequestHeader('Content-Type', 'application/x-www-form-url');
								request.addEventListener("readystatechange", () => {
									if (request.readyState === 4 && request.status === 200) {
										//console.log(request.responseText);							
										var result = JSON.parse(request.responseText);
										if(result.error=="true") {
											// error true
										} else {
											if(result.hash==teachersAllHash) {
												//console.log("sync: ok");
												checkForUpdateTeachers();
											} else {
												console.log("sync: need update");
												if(!afterChange) {
													note({
														content: "Синхронизирован",
														type: "info",
														time: 3
													});					
												}	
												afterChange=false;
												ucStarted=false;
												sqlLoadTeachers();
											}
										}
									} else if(request.readyState === 4) {
										teachersAllHash="none";
										closeTeacherInfo();
										printError("Сервер недоступен?");
										checkForUpdateTeachers();
									}
								});
								request.send();									
							};
							return 1;
						});
					}
				}
				var tempTeacher;
				var tempTeacherName;
				var tempTeacherId;
				function updateTeacher() {
					var teacherIdInput = document.getElementById("teacherIdInput");
					var fullNameInput = document.getElementById("fullNameInput");
					tempTeacher = {
						id: teacherIdInput.value,
						fullName: fullNameInput.value
					}
					if(tempTeacherName==tempTeacher.fullName) {
						//alert("Вы не изменили ФИО преподавателя");
						note({
						  content: "Вы не изменили ФИО преподавателя",
						  type: "warn",
						  time: 3
						});						
						return;
					}
					$.MessageBox(
						{
							buttonDone  :"Да",
							buttonFail  :"Отмена",
							message     :"Вы точно хотите изменить ФИО этого преподавателя?"
						}).done(function(){
							closeTeacherInfo();
							printInfo("Запрос выполняется");
							updateTeacherSql(tempTeacher.id,tempTeacher.fullName);
						}).fail(function(){
							//
						}
					);
				}
				function updatePair() {
					//tempPairId;
					//tempPairName;
					//tempPairTid;
					var pairNameInput=document.getElementById("pairNameInput");
					var pairTeacherSelect=document.getElementById("selectPairsTeacher");
					var tid=pairTeacherSelect[pairTeacherSelect.selectedIndex].getAttribute("tid");
					//console.log("Old: "+tempPairId+" | "+tempPairName+" | "+tempPairTid);
					//console.log("New: "+tempPairId+" | "+pairNameInput.value+" | "+tid);
					var updateName=!(tempPairName==pairNameInput.value);
					var updateTid=!(tempPairTid==tid);
					if((!updateName)&(!updateTid)) {
						printWarn("Вы не изменили преподавателя или название пары");
						return;
					}
					var type="";
					if(updateName&updateTid) {
						type="both";
					} else if(updateName) {
						type="name";
					} else if(updateTid) {
						type="tid";
					}
					var hPid = tempPairId;
					var hPtid = tempPairTid;
					$.MessageBox(
						{
							buttonDone  :"Да",
							buttonFail  :"Отмена",
							message     :"Вы точно хотите обновить "+
							(type=="both"?"название и преподавателя":(type=="name"?"название":"преподавателя"))+" у выбранной пары?"
						}).done(function(){
							cancelPairInfo();
							printInfo("Запрос выполняется");
							deletePairFromDom(hPid,hPtid);						
							updatePairSql(type,hPid,pairNameInput.value,tid);
						}).fail(function(){
							//
						}
					);	
				}
				function deletePair() {
					var unknownPair=true;
					if(tempPairTid!=undefined) {
						if(tempPairTid!="0") {	
							unknownPair=false;
						}
					}
					var hPid = tempPairId;
					var hPtid = tempPairTid;
					$.MessageBox(
						{
							buttonDone  :"Да",
							buttonFail  :"Отмена",
							message     :"Вы точно хотите хотите удалить выбранную пару"+(unknownPair?"?":" у этого преподавателя?")
						}).done(function(){
							cancelPairInfo();
							printInfo("Запрос выполняется");
							deletePairFromDom(hPid,hPtid);
							updatePairSql("delete",hPid);
						}).fail(function(){
							//
						}
					);				
				}
				function deletePairFromDom(pid,ptid) {
					console.log("Удаляем..");
					if(ptid==undefined) {
						ptid=0;
					}
					var pairs;
					if(ptid>0) {
						pairs = document.getElementById("knDiv").getElementsByClassName("knPair");
					} else {
						pairs = document.getElementById("unkDiv").getElementsByClassName("unkPair");
					}
					for(var i=(pairs.length-1);i>=0;i--) {
						if(i<0) {
							return;
						}
						if(pairs[i].getAttribute("pid")==pid) {
							pairs[i].remove();
						}
					}
				}
				function updatePairSql(type, pid, pname, ptid) {
					var password = document.getElementById("passwordInp");
					var sqlRequest={
						password:MD5(password.value.trim()),
						type:type,
						pairID:pid,
						pairName:pname,
						pairTeacherID:ptid
					};					
					var request = new XMLHttpRequest();
					var url = "sql.php?type=updatepair";
					var nonError=false;
					request.open('POST', url);
					request.setRequestHeader('Content-Type', 'application/json');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							console.log("GOOD RESPONSE: "+request.responseText);
							var result = JSON.parse(request.responseText);
							var badPass=false;
							if(result.error=="true") {
								switch(result.errorType) {
									case("badpass"):
										badPass=true;
										//alert("Ошибка | Неправильный пароль");
										note({
											content: "Ошибка | Неправильный пароль",
											type: "error",
											time: 3
										});
									break;
									case("badid"):
										//alert("Ошибка | Указан несуществующий преподаватель");
										note({
											content: "Ошибка | Указан несуществующий преподаватель",
											type: "warn",
											time: 3
										});										
									break;
									case("badpid"):
										//alert("Ошибка | Несуществующий iD пары, играетесь с F12?");
										note({
											content: "Ошибка | Несуществующий iD пары, играетесь с F12?",
											type: "error",
											time: 3
										});										
									break;									
									case("busyby"):
										note({
											content: "Ошибка | Указанная пара уже принадлежит другому"+
											" преподавателю ["+result.desc+"] "+"\n"+
											"Её нельзя добавить в список неизвестных"+"\n"+
											"Выберите конкретного преподавателя для её добавления",
											type: "warn",
											time: 10
										});
									break;
									case("already_unkwn"):
										//alert("Ошибка | Указанная пара уже добавлена в список неизвестных");
										note({
											content: "Ошибка | Указанная пара уже добавлена в список неизвестных",
											type: "warn",
											time: 4
										});																				
									break;
									case("already"):
										//alert("Ошибка | Указанная пара уже принадлежит этому преподавателю");
										note({
											content: "Ошибка | Указанная пара уже принадлежит этому преподавателю",
											type: "warn",
											time: 4
										});											
									break;
									case("empty"):
										//alert("Ошибка | Название пары не может быть пустым или < 1 Буквы");
										note({
											content: "Ошибка | Название пары не может быть пустым или < 1 Буквы",
											type: "warn",
											time: 4
										});											
									break;									
									default:
										//alert("Ошибка | unknown error["+result.errorType+"]");
										note({
											content: "Ошибка | unknown error["+result.errorType+"]",
											type: "error",
											time: 3
										});
									break;
								}
							} else {
								// Good
								afterChange=true;
								switch(result.type) {
									case("deleted"):
										note({
											content: "Пара удалена!",
											type: "success",
											time: 3
										});
									break;
									case("both"):
										note({
											content: "Название и преподаватель пары обновлен!",
											type: "success",
											time: 3
										});
									break;
									case("name"):
										note({
											content: "Название пары обновлено!",
											type: "success",
											time: 3
										});
									break;
									case("tid"):
										note({
											content: "Преподаватель пары обновлен!",
											type: "success",
											time: 3
										});
									break;
									default:
										//alert("Пара обновлена!");
										note({
											content: "Пара обновлена!",
											type: "success",
											time: 3
										});										
									break;
								}
							}
							closeInfo();
						} else if(request.readyState === 4) {
							printError("Не удалось выполнить запрос,\nДоступен ли сервер?");
							// ADD CHECK CONNECT TO SERVER
						}
					});
					request.send(JSON.stringify(sqlRequest));
				}
				function deleteTeacher() {
					var teacherIdInput = document.getElementById("teacherIdInput");
					var fullNameInput = document.getElementById("fullNameInput");
					tempTeacher = {
						id: teacherIdInput.value,
						fullName: fullNameInput.value
					}
					$.MessageBox(
						{
							buttonDone  :"Да",
							buttonFail  :"Отмена",
							message     :"Вы точно хотите хотите удалить этого преподавателя?"
						}).done(function(){
							closeTeacherInfo();
							printInfo("Запрос выполняется");
							deleteTeacherSql(tempTeacher.id);
						}).fail(function(){
							//
						}
					);					
				}	
				function deleteTeacherSql(id) {
					cancelTeacherInfo();
					var password = document.getElementById("passwordInp");
					var sqlRequest={
						password:MD5(password.value.trim()),
						id:id
					};					
					var request = new XMLHttpRequest();
					var url = "sql.php?type=deleteteacher";
					var nonError=false;
					request.open('POST', url);
					request.setRequestHeader('Content-Type', 'application/json');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							console.log("GOOD RESPONSE: "+request.responseText);
							var result = JSON.parse(request.responseText);
							var badPass=false;
							if(result.error=="true") {
								switch(result.errorType) {
									case("badpass"):
										badPass=true;
										//alert("Ошибка | Неправильный пароль");
										note({
											content: "Ошибка | Неправильный пароль",
											type: "error",
											time: 3
										});
									break;
									case("badid"):
										//alert("Ошибка | Преподаватель уже удален или произошла внутренняя ошибка");
										note({
											content: "Ошибка | Преподаватель уже удален или произошла внутренняя ошибка",
											type: "warn",
											time: 3
										});										
									break;
									default:
										//alert("Ошибка | unknown error["+result.errorType+"]");
										note({
											content: "Ошибка | unknown error["+result.errorType+"]",
											type: "error",
											time: 3
										});
									break;
								}
							} else {
								// Good
								afterChange=true;
								switch(result.type) {
									default:
										//alert("Преподаватель удален!");
										note({
											content: "Преподаватель удален!",
											type: "success",
											time: 3
										});										
									break;
								}
							}
							closeInfo();
							if(!badPass) {
								removeAllTeachers();
							}
						} else if(request.readyState === 4) {
							printError("Не удалось выполнить запрос,\nДоступен ли сервер?");
							// ADD CHECK CONNECT TO SERVER
						}
					});
					request.send(JSON.stringify(sqlRequest));					
				}				
				function updateTeacherSql(id,newName) {
					cancelTeacherInfo();
					var password = document.getElementById("passwordInp");
					var sqlRequest={
						password:MD5(password.value.trim()),
						id:id,
						teacherName:newName
					};					
					var request = new XMLHttpRequest();
					var url = "sql.php?type=updateteacher";
					var nonError=false;
					request.open('POST', url);
					request.setRequestHeader('Content-Type', 'application/json');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							console.log("GOOD RESPONSE: "+request.responseText);
							var result = JSON.parse(request.responseText);
							if(result.error=="true") {
								switch(result.errorType) {
									case("badpass"):
										//alert("Ошибка | Неправильный пароль");
										note({
											content: "Ошибка | Неправильный пароль",
											type: "error",
											time: 3
										});											
									break;
									case("empty"):
										//alert("Ошибка | Имя не может быть пустым или < 2 Букв");
										note({
											content: "Ошибка | Имя не может быть пустым или < 2 Букв",
											type: "warn",
											time: 3
										});											
									break;
									case("already"):
										//alert("Ошибка | Данный преподаватель уже добавлен");
										note({
											content: "Ошибка | Данный преподаватель уже добавлен",
											type: "warn",
											time: 3
										});
									break;
									case("badid"):
										//alert("Ошибка | Преподаватель удален или произошла внутренняя ошибка");
										note({
											content: "Ошибка | Преподаватель удален или произошла внутренняя ошибка",
											type: "warn",
											time: 3
										});
									break;
									default:
										//alert("Ошибка | unknown error["+result.errorType+"]");
										note({
											content: "Ошибка | unknown error["+result.errorType+"]",
											type: "error",
											time: 3
										});										
									break;
								}
							} else {
								// Good
								afterChange=true;
								switch(result.type) {
									case("updated"):
										if(result.desc!=undefined) {
											var teacherData = result.desc.split(",");
											addTeacher(teacherData[0],teacherData[1]);
										}
									default:
										//alert("Имя преподавателя обновлено!");
										note({
											content: "Имя преподавателя обновлено!",
											type: "success",
											time: 3
										});										
									break;
								}
							}
							closeInfo();
						} else if(request.readyState === 4) {
							printError("Не удалось выполнить запрос,\nДоступен ли сервер?");
							// ADD CHECK CONNECT TO SERVER
						}
					});
					request.send(JSON.stringify(sqlRequest));					
				}
				function closeTeacherInfo() {
					var teacherIdInput = document.getElementById("teacherIdInput");
					var fullNameInput = document.getElementById("fullNameInput");
					tempTeacher = {
						id: teacherIdInput.value,
						fullName: fullNameInput.value
					}
					teacherShowWindow.style.display="none";
					teacherShows=false;
				}
				function closePairInfo() {
					var pairIdInput = document.getElementById("pairIdInput");
					var pairNameInput = document.getElementById("pairNameInput");
					var selectPairsTeacher = document.getElementById("selectPairsTeacher");
					var ptid=selectPairsTeacher.options[selectPairsTeacher.selectedIndex].getAttribute("tid");
					if(ptid==undefined) {
						ptid=0;
					} else if(ptid.trim().length<0) {
						ptid=0;
					}
					tempPair = {
						id: pairIdInput.value,
						name: pairNameInput.value,
						teacherID: ptid
					}
					pairShowWindow.style.display="none";
					pairShows=false;
				}				
				function cancelTeacherInfo() {
					var teacherShowWindow = document.getElementById("teacherShowWindow");
					tempTeacher=undefined;
					tempTeacherName=undefined;
					tempTeacherId=undefined;
					teacherShowWindow.style.display="none";
					teacherShows=false;
				}
				function cancelPairInfo() {
					var pairShowWindow = document.getElementById("pairShowWindow");
					tempPair=undefined;
					tempPairName=undefined;
					tempPairTid=undefined;
					selectPairsTeacher.selectedIndex=0;
					pairShowWindow.style.display="none";
					pairShows=false;
				}
				function sleep(ms) {
					return new Promise(resolve => setTimeout(resolve, ms));
				}
				function fade(el) {
					var teacherP = el.getElementsByClassName("teacherP")[0];
					teacherP.style.borderStyle="inset";
					return false;
				}
				function addTeacherPairGui(first) {
					if(first==undefined) {
						first=true;
					}
					$.MessageBox({
					input    :true,
					buttonFail  :"Отмена",
					message  :"Введите название новой пары преподавателя"+(first?"":" (Название не может быть пустым)")
					}).done(function(data){
						console.log(data);
						if($.trim(data)) {
							if(data.trim().length<1) {
								addTeacherPairGui(false);
							} else {
								printInfo("Запрос выполняется");
								var teacherIdInput = document.getElementById("teacherIdInput");								
								addPairSql(data.trim(),teacherIdInput.value);	
							}
						} else {
							addTeacherPairGui(false);
						}
					});
				}
				function addPairGui(first) {
					if(first==undefined) {
						first=true;
					}
					$.MessageBox({
					input    :true,
					buttonFail  :"Отмена",
					message  :"Введите название пары"+(first?"":" (Название не может быть пустым)")
					}).done(function(data){
						console.log(data);
						if($.trim(data)) {
							if(data.trim().length<1) {
								addPairGui(false);
							} else {
								printInfo("Запрос выполняется");
								addPairSql(data.trim());	
							}
						} else {
							addPairGui(false);
						}
					});
				}
				function addPairSql(pairName, tid) {
					if(tid==undefined) {
						tid=0;
					}
					var password = document.getElementById("passwordInp");
					var sqlRequest={
						password:MD5(password.value.trim()),
						name:pairName,
						id:tid
					};					
					var request = new XMLHttpRequest();
					var url = "sql.php?type=addpair";
					var nonError=false;
					request.open('POST', url);
					request.setRequestHeader('Content-Type', 'application/json');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							console.log("GOOD RESPONSE: "+request.responseText);
							var result = JSON.parse(request.responseText);
							if(result.error=="true") {
								switch(result.errorType) {
									case("badpass"):
										//alert("Ошибка | Неправильный пароль");
										note({content: "Ошибка | Неправильный пароль",
											type: "error",time: 3});											
									break;
									case("busyby"):
										note({
											content: "Ошибка | Указанная пара уже принадлежит другому"+
											" преподавателю ["+result.desc+"] "+"\n"+
											"Её нельзя добавить в список неизвестных"+"\n"+
											"Выберите конкретного преподавателя для её добавления",
											type: "warn",
											time: 10
										});
									break;
									case("already_unkwn"):
										//alert("Ошибка | Указанная пара уже добавлена в список неизвестных");
										note({
											content: "Ошибка | Указанная пара уже добавлена в список неизвестных",
											type: "warn",
											time: 4
										});																				
									break;
									case("already"):
										//alert("Ошибка | Указанная пара уже принадлежит этому преподавателю");
										note({
											content: "Ошибка | Указанная пара уже принадлежит этому преподавателю",
											type: "warn",
											time: 4
										});											
									break;
									case("empty"):
										//alert("Ошибка | Название пары не может быть пустым или < 1 Буквы");
										note({
											content: "Ошибка | Название пары не может быть пустым или < 1 Буквы",
											type: "warn",
											time: 4
										});											
									break;
									case("badid"):
										//alert("Ошибка | Указан несуществующий идентификатор преподавателя");
										note({
											content: "Ошибка | Указан несуществующий идентификатор преподавателя",
											type: "error",
											time: 4
										});											
									break;
									default:
										//alert("Ошибка | unknown error["+result.errorType+"]");
										note({
											content: "Ошибка | unknown error["+result.errorType+"]",
											type: "error",
											time: 4
										});											
									break;
								}
							} else {
								// Good
								afterChange=true;
								switch(result.type) {
									case("added"):									
										//
									default:
										//alert("Пара добавлена!");
										note({
											content: "Пара добавлена!",
											type: "success",
											time: 3
										});
									break;
								}
							}
							closeInfo();
						} else if(request.readyState === 4) {
							printError("Не удалось выполнить запрос,\nДоступен ли сервер?");
							// ADD CHECK CONNECT TO SERVER
						}
					});
					request.send(JSON.stringify(sqlRequest));					
				}
				function addTeacherGui(first) {					
					if(first==undefined) {
						first=true;
					}
					$.MessageBox({
					input    :true,
					buttonFail  :"Отмена",
					message  :"Введите ФИО нового преподавателя"+(first?"":" (ФИО не может быть пустым)")
					}).done(function(data){
						console.log(data);
						if($.trim(data)) {
							if(data.trim().length<1) {
								addTeacherGui(false);
							} else {
								printInfo("Запрос выполняется");
								addTeacherSql(data.trim());	
							}
						} else {
							addTeacherGui(false);
						}
					});					
				}
				function addTeacherSql(name) {
					var password = document.getElementById("passwordInp");
					var sqlRequest={
						password:MD5(password.value.trim()),
						teacherName:name
					};					
					var request = new XMLHttpRequest();
					var url = "sql.php?type=addteacher";
					var nonError=false;
					request.open('POST', url);
					request.setRequestHeader('Content-Type', 'application/json');
					request.addEventListener("readystatechange", () => {
						if (request.readyState === 4 && request.status === 200) {
							console.log("GOOD RESPONSE: "+request.responseText);
							var result = JSON.parse(request.responseText);
							if(result.error=="true") {
								switch(result.errorType) {
									case("badpass"):
										//alert("Ошибка | Неправильный пароль");
										note({
											content: "Ошибка | Неправильный пароль",
											type: "error",
											time: 3
										});											
									break;
									case("already"):
										//alert("Ошибка | Данный преподаватель уже добавлен");
										note({
											content: "Ошибка | Данный преподаватель уже добавлен",
											type: "warn",
											time: 3
										});											
									break;
									case("empty"):
										//alert("Ошибка | Имя не может быть пустым или < 2 Букв");
										note({
											content: "Ошибка | Имя не может быть пустым или < 2 Букв",
											type: "warn",
											time: 3
										});											
									break;
									default:
										//alert("Ошибка | unknown error["+result.errorType+"]");
										note({
											content: "Ошибка | unknown error["+result.errorType+"]",
											type: "error",
											time: 3
										});											
									break;
								}
							} else {
								// Good
								afterChange=true;
								switch(result.type) {
									case("added"):									
										if(result.desc!=undefined) {
											var teacherData = result.desc.split(",");
											addTeacher(teacherData[0],teacherData[1]);
										}
									default:
										//alert("Преподаватель добавлен!");
										note({
											content: "Преподаватель добавлен!",
											type: "success",
											time: 3
										});
									break;
								}
							}
							closeInfo();
						} else if(request.readyState === 4) {
							printError("Не удалось выполнить запрос,\nДоступен ли сервер?");
							// ADD CHECK CONNECT TO SERVER
						}
					});
					request.send(JSON.stringify(sqlRequest));
				}
				function removeAllTeachers() {
					var teachersContainer = document.getElementById("teachers");
					var teachersArray = teachersContainer.getElementsByClassName("teacher");
					for(var i=(teachersArray.length-1);i>=0;i--) {
						if(i<0) {
							return;
						}
						teachersArray[i].remove();
					}
					teachersAllHash="none";
				}
				function removeAllTeachersOptions() {
					var teachersOptions = document.getElementById("selectPairsTeacher");
					var teachersOptionsArray = teachersOptions.options;
					for(var i=(teachersOptionsArray.length-1);i>=0;i--) {
						if(i<0) {
							return;
						}
						var tid = teachersOptionsArray[i].getAttribute("tid");
						if(tid=="0") {
							continue;
						}
						teachersOptionsArray[i].remove();
					}				
				}
				function addTeacherOption(id, fullName) {
					var selectPairsTeacher = document.getElementById("selectPairsTeacher");
					var teacherOptionsArray = selectPairsTeacher.getElementsByClassName("teacherOption");
					for(teacherOption of teacherOptionsArray) {
						if(teacherOption.getAttribute("tID")==id) {
							teacherOption.textContent=fullName;
							return;
						}
					}
					var teacherOption = document.createElement("option");
					teacherOption.setAttribute("tid",id);
					teacherOption.textContent=fullName;
					selectPairsTeacher.appendChild(teacherOption);
				}
				function addTeacher(id, fullName) {
					addTeacherOption(id,fullName);
					var teachersContainer = document.getElementById("teachers");
					var teachersArray = teachersContainer.getElementsByClassName("teacher");
					for(teacherContainer of teachersArray) {
						if(teacherContainer.getAttribute("tID")==id) {
							var spanFullName = teacherContainer.getElementsByClassName("fullName")[0];
							spanFullName.textContent=fullName;
							tempTeacher=undefined;
							tempTeacherName=undefined;
							return;
						}
					}
					var teacherDiv = document.createElement("div");
						teacherDiv.classList.add("teacher");
						teacherDiv.setAttribute("tID",id);
						teacherDiv.setAttribute("onmousedown", "fade(this)");
						teacherDiv.setAttribute("onclick", "showTeacher(this)");
					var teacherP = document.createElement("p");
						teacherP.classList.add("teacherP");
					var teacherSpan = document.createElement("span");
						teacherSpan.classList.add("fullName");
						teacherSpan.textContent=fullName.trim();
					teacherP.appendChild(teacherSpan);
					teacherDiv.appendChild(teacherP);
					teachersContainer.appendChild(teacherDiv);
				}
				var canShow=true;
				var teacherShows=false;
				function showTeacher(el) {
					if(!canShow) {
						return;
					}
					teacherShows=true;
					canShow=false;
					var teacherShowWindow = document.getElementById("teacherShowWindow");
					var fullName = el.getElementsByClassName("fullName")[0].textContent;
					tempTeacherName=fullName;
					var teacherObject = {
						id:el.getAttribute("tID"),
						fullName:fullName
					}
					tempTeacherId=teacherObject.id;
					var teacherP = el.getElementsByClassName("teacherP")[0];
					teacherP.style.borderStyle="outset";
					var teacherIdInput = document.getElementById("teacherIdInput");
					var fullNameInput = document.getElementById("fullNameInput");					
					if(tempTeacher!=undefined) {
						if(tempTeacher.id==teacherObject.id) {
							fullNameInput.value=tempTeacher.fullName;
							teacherIdInput.value=tempTeacher.id;
							loadTeacherPairs(tempTeacher.id);
							teacherShowWindow.style.display="flex";
							canShow=true;
							return;
						}
					}
					fullNameInput.value=teacherObject.fullName;
					teacherIdInput.value=teacherObject.id;
					loadTeacherPairs(teacherObject.id);
					teacherShowWindow.style.display="flex";
					canShow=true;
				}
				function loadTeacherPairs(tid) {
					if(tid==undefined) {
						tid=0;
					}
					if(tid<1) {
						printWarn("Функция принимает аргументы только > 0")
					}
					removeAllTeacherPairs();
					if(teachersArray!=null) {
						var knDiv = document.getElementById("knDiv");
						pairsAllArray.forEach(function(pair) {
							if(tid==pair.teacherID) {
								var pPair = document.createElement("p");
								pPair.setAttribute("pid",pair.id);
								pPair.setAttribute("onclick","showPair(this)");
								pPair.setAttribute("class","knPair");
								pPair.setAttribute("tpid",pair.teacherID);
								pPair.textContent=pair.name;
								knDiv.appendChild(pPair);
							}
						});
					} 
				}
				function removeAllTeacherPairs() {
					var knDivDom = document.getElementById("knDiv");
					var knpairs = knDivDom.getElementsByClassName("knPair");
					for(var i=(knpairs.length-1);i>=0;i--) {
						if(i<0) {
							return;
						}
						knpairs[i].remove();
					}
				}
				function printWarn(message) {
					note({
						content: message,
						type: "warn",
						time: 3
					});
				}
				var tempPairName;
				var tempPair;
				var tempPairId;
				var tempPairTid;
				function selectTeacherOption(ptid) {
					var teacherOptions = document.getElementById("selectPairsTeacher");
					for(teacherOption of teacherOptions.options) {
						if((teacherOption.getAttribute("tid"))==ptid) {
							teacherOptions.selectedIndex=teacherOption.index;
							return;
						}
					}
					teacherOptions.selectedIndex=0;
				}
				var pairShows=false;
				function showPair(el) {
					pairShows=true;
					var pairShowWindow = document.getElementById("pairShowWindow");
					var pairId = el.getAttribute("pid");
					var pairName = el.textContent.trim();
					tempPairName=pairName;
					var selectPairsTeacher = document.getElementById("selectPairsTeacher");
					var ptid=el.getAttribute("tpid");
					if(ptid==undefined) {
						ptid=0;
					} else if(ptid.trim().length<0) {
						ptid=0;
					}
					tempPairTid=ptid;
					tempPairId=pairId;
					var pairObject = {
						id:pairId,
						name:pairName,
						teacherID:ptid
					}
					var pairIdInput=document.getElementById("pairIdInput");
					var pairNameInput=document.getElementById("pairNameInput");
					var pairTeacherSelect=document.getElementById("selectPairsTeacher");
					if(tempPair!=undefined) {
						if(tempPair.id==pairObject.id) {
							pairIdInput.value=tempPair.id;
							pairNameInput.value=tempPair.name;
							selectTeacherOption(tempPair.teacherID);
							pairShowWindow.style.display="flex";
							return;
						} 
					}		
					selectPairsTeacher.selectedIndex=0;
					pairIdInput.value=pairObject.id;	
					pairNameInput.value=pairObject.name;
					selectTeacherOption(ptid);					
					console.log(pairObject);
					pairShowWindow.style.display="flex";
				}
				 window.onload = function() {
					console.log("mainLogic loaded..");
					//disableLoader(true);
					//showContent(true);
					$("#botSettings" ).draggable();
					$("#pairSettings" ).draggable();
					$("#teachersForRemove" ).draggable();
					$("#pairForRemove" ).draggable();
					var inputTeacherName = document.getElementById("inputTeacherName");
					inputTeacherName.oninput = function() {
						//console.log(inputGroup.value);
						var teachers = Array.from(document.getElementsByClassName("teacher"));					
						teachers.forEach(function(teacher) {
							var name = teacher.getElementsByClassName("fullName")[0].textContent.toLowerCase();
							if(((name.indexOf(inputTeacherName.value.toLowerCase()))==-1)) {
								teacher.classList.add("hide");
							} else {
								teacher.classList.remove("hide");
							}
						});		
					};		
					sqlLoadTeachers();
					sqlLoadPairs();
				};
