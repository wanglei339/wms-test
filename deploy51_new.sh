#!/bin/bash

#发布的APP
APPS=( "wms-integration" "wms-rpc" "wms-task" "wms-provider" "wms-rf" "wms-worker")
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

#环境选择
echo "部署59/51?[59: 1; 51: 2]: "
read env
if [ "$env" = "2" ]
then
  REMOTE="work@192.168.60.51"
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
    ssh $REMOTE "rsync -avzP --exclude=log --exclude=logs --exclude=out.log /home/work/lsh-wms-wl/${APP}/* /home/work/lsh-wms-wl/${APP}.bak"
    ssh $REMOTE "rm -f /home/work/lsh-wms-wl/$APP/lib/wms-*.jar && rm -f /home/work/lsh-wms-wl/$APP/lib/base-common-1.0-SNAPSHOT.jar && rm -rf /home/work/lsh-wms-wl/$APP/conf/com"
    echo "========== BACKUP ${APP} DONE. ==============="

    rsync -avzP $APPROOT/$APP-$VERSION-dev/lib/wms-*.jar $REMOTE:/home/work/lsh-wms-wl/$APP/lib
    rsync -avzP $APPROOT/$APP-$VERSION-dev/lib/base-common-*.jar $REMOTE:/home/work/lsh-wms-wl/$APP/lib
    rsync -avzP $APPROOT/$APP-$VERSION-dev/conf/com $REMOTE:/home/work/lsh-wms-wl/$APP/conf
    rsync -avzP $APPROOT/$APP-$VERSION-dev/conf/props/exception.properties $REMOTE:/home/work/lsh-wms-wl/$APP/conf/props
    ssh $REMOTE "sh /home/work/lsh-wms-wl/$APP/bin/run.sh"
    ssh $REMOTE "tail -f /home/work/lsh-wms-wl/$APP/log/provider.log"
    rm -rf $APPROOT/$APP-$VERSION-dev
  fi
done
