@echo off

rem This is just a wrapper that makes g2fbook.sh usable under Windows

set PATH=.\cygwin-rt;%PATH%
bash -e ./g2fbook.sh %1 %2