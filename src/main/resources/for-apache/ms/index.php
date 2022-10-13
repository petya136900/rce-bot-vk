<!DOCTYPE html>
<html>
	<head>
		<title>RCE Bot  | Основное расписания</title>
		<meta charset="UTF-8">
		<script src="../js/rkebot/plugins.minClear.js"></script>
		<script src="../js/jquery.js"></script>
		<script src="../js/main.js"></script>		
		<link href="../css/highstl.css" rel="stylesheet">	
		<link rel="stylesheet" type="text/css" href="../assets/css/fonts.v1.css">
		<link rel="stylesheet" type="text/css" href="../assets/css/style.min.v30.v2.css">	
		<link rel="stylesheet" href="../css/rke/animloader.css">		
		<link rel="stylesheet" href="slide.css">
		<link rel="stylesheet" href="style.css">
		<script src="mainLogic.js"></script>
		<script  src="slide.js"></script>
	</head>
	<body>
	<div id="star" class=" "> <!-- Space background -->
		<canvas class="cover"></canvas>
	</div>
	<div id="botSettings">
		<button id="closeSettings">X</button>
		<button id="saveSettings">Save</button>
		<button id="cancelSettings">Cancel</button>
		<h2 id="settingsH">Настройки:</h2>
		<div id="settingsList">
			<p class="setting">
				<span class="varName">
					enabled
				</span>
				<span class="varType">
					(Boolean)
				</span>
				<input type="checkbox" checked="checked">
			</p>
			<p class="setting">
				<span class="varName">
					domain
				</span>
				<span class="varType">
					(String)
				</span>
				<input value="https://ркэ.рф/">
			</p>			
		</div>
	</div>
	<div id="all">
		<div id="cont">
			<form action="../">
				<button id="ButtonBackPage" class="ButtonDrive" type="submit" name="backPage" value="Назад" type="submit">Назад</button>
			</form>
			<h1 id="logo">RCE BOT  | Изменение основного расписания</br><span class="right">@petya136900</span></h1>
			<div class="input center">
				<span>Группа </span>
				<input type="text" id="inputUserID" size="6" maxlength="16" value="<?php echo empty($selectedGroup)?"":$selectedGroup;?>" name="selectedGroup">
				<input id="findGroup" type="submit" value="Найти">
			</div>
			<div id="panel">
				<div class="partPanel">
						<span><h1 class="green">Добавить группу:</h1></span>
						<input type="text" id="groupForAdd" size="6" maxlength="16" value="" name="addedGroup">
						<input disabled class="buttonPanel" id="createButt" type="submit" onclick="createBlock(null,null,null,true)" value="Добавить">
						<!-- --- - - - -  - -- ------- - -->
						</br>
						</br>
						<span><h1 class="red"> Удалить группу:</h1></span>
						<input type="text" id="groupForDelete" size="6" maxlength="16" value="" name="deletedGroup">
						<input disabled class="buttonPanel" id="removeButt" type="submit" onclick="removeBlock(null,true)" value="Удалить" form="auth">
					<p class="tip">@TIP: Введите * для удаления всех групп</p>
				</div>
				<div id="settingsPanel">
					<div id="slide">
						<div class="containerSld">
							<p class="sld" id="pairsSld"> Выкл </p><span class="item"></span><p class="sld" id="practicsSld"> Вкл </p>
						</div>	
						<input id="hid" value="<?php if($sqlReplaceMode==0){
								echo 'false';
							} else {
								echo 'true';
							}?>" form="auth" name="workMode">
					</div>	
					<p class="tip2">@TIP: Если выключено, </br>Бот будет уведомлять, </br>что основное расписание </br> в процессе изменения</p>
					<a id="addSettings">Доп. настройки</a>
				</div>
				<script>
					App.init();
					Kek = document.getElementById( "hid" );
					if(Kek.value=='false') {
						App.toggleIt();
					}
						if(App.state==false) {
							Kek.value="true";
						} else {
							Kek.value="false";
						}
					function toggleIt2() {
						if( App.state){
							Kek.value="false";
						}else{
							Kek.value="true";
						}
					};
					App.container.addEventListener( 'click', this.toggleIt2 );
				</script>
			</div>
			<div id="onLoadAnim">
				<span id="loadingText">Загрузка...</span>
				<div id="main">
					<div class="banter-loader">
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					  <div class="banter-loader__box"></div>
					</div>				
				</div>
			</div>				
			<div class=""></div>
			<div id="mainCenter" class="hide">
					<h1 id="pass">Pass</h1>:<input id="passwordInp" autocomplete="off" type="password" size="3" maxlength="32" value="" required>
					<p><input id="sumButton" type="submit" onclick="writeGroups()" value="Перезаписать"></p>
					<div id="monday">
						<h1 class="weekDay">Понедельник</h1>
					</div>
					<div id="tuesday">
						<h1 class="weekDay">Вторник</h1>
					</div>					
					<div id="wednesday">
						<h1 class="weekDay">Среда</h1>
					</div>					
					<div id="thursday">
						<h1 class="weekDay">Четверг</h1>
					</div>					
					<div id="friday">
						<h1 class="weekDay">Пятница</h1>
					</div>					
					<div id="saturday">
						<h1 class="weekDay">Суббота</h1>
					</div>									
			</div>
		</div>
			<p id="fix"></p>
		</div>
	</div>
	</body>
</html>
