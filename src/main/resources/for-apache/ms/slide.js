var App = {
            createView: function (){
                this.container = document.getElementsByClassName( "containerSld" )[0];
                this.item = document.getElementsByClassName( "item" )[0];
				this.pairsLine = document.getElementById( "pairsSld" );
				this.practicsLine = document.getElementById( "practicsSld" );
                this.state = false;
            },

            toggleIt: function ( ev ) {
                if( App.state ){
					App.pairsLine.style.color = "#42648b";
					App.practicsLine.style.color = "gray";					
                    App.item.style.left = "2%";
                    App.item.style.animation= "moveL .4s";
                    App.state = false;
                }else{
					App.pairsLine.style.color = "gray";
					App.practicsLine.style.color = "#42648b";
                    App.item.style.left = "47px";
                    App.item.style.animation= "move .4s";
                    App.state = true;
                }
            },

            bindEv: function () {
                this.container.addEventListener( 'click', this.toggleIt );
            },

            init: function (){
                this.createView();
                this.bindEv();
            }
        };