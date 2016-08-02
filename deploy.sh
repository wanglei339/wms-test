#!/bin/bash

#发布的APP
#APP="wms-rpc"
#APP="wms-task"
#APP="wms-provider"
APP="wms-rf"
#本地路径
ROOT="/Users/fengkun/Projects/wms/wms/${APP}/target"
#版本号
VERSION="1.0-SNAPSHOT"
#远程路径
REMOTE="work@192.168.60.59"

tar -zxvf $ROOT/$APP-$VERSION-dev.tar.gz -C $ROOT/
echo "========== BACKUP ${APP} BEGIN... ============"
ssh $REMOTE "rsync -avzP --exclude=log --exclude=out.log /home/work/lsh-wms/${APP}/* /home/work/lsh-wms/${APP}.bak"
ssh $REMOTE "rm -f /home/work/lsh-wms/$APP/lib/wms-*.jar && rm -rf /home/work/lsh-wms/$APP/conf/com"
echo "========== BACKUP ${APP} DONE. ==============="

scp $ROOT/$APP-$VERSION-dev/lib/wms-*.jar $REMOTE:/home/work/lsh-wms/$APP/lib
scp -r $ROOT/$APP-$VERSION-dev/conf/com $REMOTE:/home/work/lsh-wms/$APP/conf
ssh $REMOTE "sh /home/work/lsh-wms/$APP/bin/run.sh"
rm -rf $ROOT/$APP-$VERSION-dev
