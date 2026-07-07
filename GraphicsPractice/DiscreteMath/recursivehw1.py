import turtle

pen = turtle.Turtle()


        

def theShape(x, y, size):
    pen.goto(x,y)
    pen.forward(size/2)
    pen.left(90)
    pen.forward(size/2)
    pen.right(90)
    pen.forward(size/2)
    pen.left(90)
    pen.forward(size/2)
    pen.left(90)
    pen.forward(size)
    pen.left(90)
    pen.forward(size)
    






def recursive(x, y, size):
    theShape(x,y,size)
    if(size == 1):
        return
    else:
        return recursive(x, y, size/2)
        


recursive(0, 0, 100)
turtle.done()


    

