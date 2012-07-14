$(document).ready ->
  colors = [ "#543534", "#343245", "#0067C8", "#984732" ]
  random = Math.floor(Math.random() * colors.length)
  $("body").css "background-color", colors[random]