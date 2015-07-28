library(scatterplot3d)
fpt_tc_kr_data<-read.csv(file.choose())

scatterplot3d(fpt_tc_kr_data$k, fpt_tc_kr_data$r, fpt_tc_kr_data$size, highlight.3d = F, col.axis = "blue", col.grid = "lightblue", main = "k-r-size", pch = 20)
scatterplot3d(fpt_tc_kr_data$k, fpt_tc_kr_data$r, fpt_tc_kr_data$time, highlight.3d = F, col.axis = "blue", col.grid = "lightblue", main = "k-r-time", pch = 20)