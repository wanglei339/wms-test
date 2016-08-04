#!/bin/bash

#发布的APP
APPS=( "wms-rpc" "wms-task" "wms-provider" "wms-rf" )
#版本号
VERSION="1.0-SNAPSHOT"
#远程路径
REMOTE="work@192.168.60.59"
#本地路径
ROOT=$(cd `dirname $0`; pwd)

#Maven打包
echo "是否要Maven打包[y/n]: "
read mvn
if [ "$mvn" = "y" -o "$mvn" = "Y" ]
then
  cd $ROOT
  mvn clean compile package
fi

#循环部署代码
for APP in ${APPS[@]}
do
  echo "是否部署${APP}模块代码[y/n]: "
  read deploy 
  if [ "$deploy" = "y" -o  "$deploy" = "Y" ]
  then
    APPROOT="${ROOT}/${APP}/target"
    tar -zxvf $APPROOT/$APP-$VERSION-dev.tar.gz -C $APPROOT/
    echo "========== BACKUP ${APP} BEGIN... ============"
    ssh $REMOTE "rsync -avzP --exclude=log --exclude=logs --exclude=out.log /home/work/lsh-wms/${APP}/* /home/work/lsh-wms/${APP}.bak"
    ssh $REMOTE "rm -f /home/work/lsh-wms/$APP/lib/wms-*.jar && rm -f /home/work/lsh-wms/$APP/lib/base-common-1.0-SNAPSHOT.jar && rm -rf /home/work/lsh-wms/$APP/conf/com"
    echo "========== BACKUP ${APP} DONE. ==============="

    scp $APPROOT/$APP-$VERSION-dev/lib/wms-*.jar $REMOTE:/home/work/lsh-wms/$APP/lib
    scp $APPROOT/$APP-$VERSION-dev/lib/base-common-1.0-SNAPSHOT.jar $REMOTE:/home/work/lsh-wms/$APP/lib
    scp -r $APPROOT/$APP-$VERSION-dev/conf/com $REMOTE:/home/work/lsh-wms/$APP/conf
    ssh $REMOTE "sh /home/work/lsh-wms/$APP/bin/run.sh"
    rm -rf $APPROOT/$APP-$VERSION-dev
  fi
done