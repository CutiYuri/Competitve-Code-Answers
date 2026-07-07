for x in range(1, 100):  
    for y in range(1, 100):
        for z in range(1, 100):
            if (((10000 - 2**x) / (2**y)) / (3**z)) == 13:
                print(x, y, z)
