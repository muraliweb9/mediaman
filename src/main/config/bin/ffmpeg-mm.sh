#!/bin/ash

INPATH='/home/karumur/media'
OUTPATH='/home/karumur/media/output'
SPATH='/home/karumur/media/scripts'
WPATH='/home/karumur/media/wlist'

echo "Searching in $MPATH"

rm -rf $WPATH/*

for entry in "$INPATH"/*.mp4 "$INPATH"/*.m4v
do
  if [ -f "$entry" ];then
    echo "Found '$entry'"
	echo "file '$entry'" >> "$WPATH"/list.txt
  fi
done

ffmpeg -f concat -i "$WPATH"/list.txt -c copy $OUTPATH/output.mp4

#ffmpeg -i input_video -f ffmetadata metadata.txt


