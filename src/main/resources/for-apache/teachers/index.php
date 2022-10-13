<!DOCTYPE html>
<html>
	<head>
		<title>RCE Bot  | Преподаватели</title>
		<meta charset="UTF-8">
		<script src="../js/rkebot/plugins.minClear.js"></script>
		<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<link rel="stylesheet" href="alerts.css">
		<script src="alerts.js"></script>
		<script src="../js/mainClear.js"></script>		
		<link href="../css/highstl.css" rel="stylesheet">	
		<link rel="stylesheet" type="text/css" href="../assets/css/style.min.v30.v2.css">	
		<link rel="stylesheet" href="../css/rke/animloader.css">		
		<link rel="stylesheet" href="style.css">
		<link rel="stylesheet" href="messagebox.css">
		<script src="mainLogic.js"></script>
		<script src="messagebox.js"></script>
	</head>
	<body>
	<div id="star" class=" "> <!-- Space background -->
		<canvas class="cover"></canvas>
	</div>
	<div id="teacherShowWindow" class="centerInfoWrapper">
		<div id="botSettings" style="display:block;">
			<button class="closeSettings" onclick="closeTeacherInfo()">X</button>
			<button class="deleteTeachers" onclick="deleteTeacher()">Delete teacher</button>
			<button class="saveSettings" onclick="updateTeacher()">Update</button>
			<button class="cancelSettings" onclick="cancelTeacherInfo()">Cancel</button>
			<h2 class="settingsH">Настройки преподавателя:</h2>
			<hr>
			<div class="settingsList">
				<p class="setting">
					<span class="varName">id</span>
					<span class="varType">(Integer)</span>
					<input disabled id="teacherIdInput" value="" size=1>
				</p>
				<p class="setting">
					<span class="varName">ФИО</span>
					<span class="varType">(String)</span>
					<input id="fullNameInput" value="" maxlength=300 size=30>
				</p>			
			</div>
			<div id="teacherPairs">
				<h2 class="settingsH">Пары преподавателя</h2>
				<hr>
				<div id="knPairs">
					<div class="partPanel pairsPanel">
						<span><h1 class="green">Добавить пару:</h1></span>
						<input class="buttonPanel" type="submit" onclick="addTeacherPairGui()" value="Добавить">
					</div>									
					<div id="knDiv"> 
						
					</div>
				</div>				
			</div>
		</div>
	</div>
	<div id="pairShowWindow" class="centerInfoWrapper">
		<div id="pairSettings" class="nsel" style="display:block;">
			<button class="closeSettings" onclick="closePairInfo()">X</button>
			<button class="deleteTeachers" onclick="deletePair()">Delete pair</button>
			<button class="saveSettings" onclick="updatePair()">Update</button>
			<button class="cancelSettings" onclick="cancelPairInfo()">Cancel</button>
			<h2 class="settingsH">Настройки пары:</h2>
			<hr>
			<div class="settingsList">
				<p class="setting">
					<span class="varName">id</span>
					<span class="varType">(Integer)</span>
					<input disabled id="pairIdInput" value="" size=1>
				</p>
				<p class="setting">
					<span class="varName">Название</span>
					<span class="varType">(String)</span>
					<input id="pairNameInput" value="" maxlength=100 size=20>
				</p>
				<p class="setting">
					<span class="varName">Преподаватель</span>
					<span class="varType">(Select)</span>
					<select id="selectPairsTeacher">
						<option class="teacherOption" tid="0">Преподаватель не выбран</option>
					</select>
				</p>		
			</div>
		</div>
	</div>
	<div id="all">
		<div id="cont">
			<form action="../">
				<button id="ButtonBackPage" class="ButtonDrive" type="submit" name="backPage" value="Назад" type="submit">Назад</button>
			</form>
			<h1 id="logo">RCE BOT  | Преподаватели</br><span class="right">@petya136900</span></h1>
			<div class="input">
				<div id="secDiv">
					<span id="phio">ФИО </span><input type="text" id="inputTeacherName" size="27" maxlength="300" value="">
				</div>
			</div>
			<div id="panel">
				<div class="partPanel">
						<span><h1 class="green">Добавить преподавателя:</h1></span>
						<input class="buttonPanel" id="createButt" type="submit" onclick="addTeacherGui()" value="Добавить">
						<!-- --- - - - -  - -- ------- - -->
				</div>
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
				<div><h1 id="pass">Pass</h1>:<input id="passwordInp" autocomplete="off" type="password" size="8" maxlength="32" value="" required></div>
				<div id="teachers">
				
				</div>
				</br>
				<hr>
				<div class="partPanel pairsPanel">
					<span><h1 class="green">Добавить пару:</h1></span>
					<input class="buttonPanel" type="submit" onclick="addPairGui()" value="Добавить">
				</div>
				<div id="unkPairs">
					<div id="unkLabel">
						<h2>Неизвестные пары</h2> 
					</div>
					<div id="unkDiv">

					</div>
				</div>
			</div>
		</div>
			<p id="fix"></p>
		</div>
	</div>
	</body>
</html>
