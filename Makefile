timestamp             = $(shell /bin/date "+%F %T")

usage:
	@echo " target           | 功能"
	@echo "------------------|---------------------------------"
	@echo " usage (default)  | 显示本菜单"
	@echo " clean            | 清理"
	@echo " push-code        | 推送代码到Github"
	@echo " change-version   | 更改版本号"
	@echo "------------------|---------------------------------"

clean:
	@mvn -f $(CURDIR)/pom.xml clean -q

push-code:
	git add .
	git commit -m "$(timestamp)"
	git push

change-version:
	@mvn -f $(CURDIR)/pom.xml versions:set
	@mvn -f $(CURDIR)/pom.xml -N versions:update-child-modules
	@mvn -f $(CURDIR)/pom.xml versions:commit

.PHONY: usage clean push-code change-version
