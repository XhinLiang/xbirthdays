build:
	(cd webui && node_modules/@angular/cli/bin/ng build --prod --aot=true) && rm -rf src/main/resources/public && mvn install
deploy:
	rsync target/xbirthdays-1.0.0.jar 58.87.71.210:/home/xhinliang/app/xbirthdays/
