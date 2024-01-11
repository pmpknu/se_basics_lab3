let graph;

function redrawGraph(r) {
    const len = 180 / 5 * r;

    const rects = graph.querySelectorAll('rect');
    rects.forEach(rect => {
        graph.removeChild(rect);
    });

    const polygons = graph.querySelectorAll('polygon');
    polygons.forEach(polygon => {
        graph.removeChild(polygon);
    });

    const paths = graph.querySelectorAll('path');
    paths.forEach(path => {
        graph.removeChild(path);
    });

    const newRect = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
    newRect.setAttribute('x', "200");
    newRect.setAttribute('y', (200 - len).toString());
    newRect.setAttribute('fill', "#05A1FF");
    newRect.setAttribute('width', (len / 2).toString());
    newRect.setAttribute('height', len.toString());

    graph.appendChild(newRect);

    const newPolygon = document.createElementNS('http://www.w3.org/2000/svg', 'polygon');
    newPolygon.setAttribute('fill', "#05A1FF");
    newPolygon.setAttribute('points', "200,200 " + (200 - len).toString() + ",200 200," + (200 + len / 2).toString());

    graph.appendChild(newPolygon);


    const newPath = document.createElementNS('http://www.w3.org/2000/svg', 'path');
    newPath.setAttribute('fill', "#05A1FF");
    newPath.setAttribute('d', "M200 " + (200 + len / 2).toString() + " A" + (len / 2).toString() + " " + (len / 2).toString() + " 0 0 0 " + (200 + len / 2).toString() + " 200 L200 200 L200 " + (200 + len / 2).toString() + " Z");

    graph.appendChild(newPath);
}

function refreshCircles() {

    const circles = graph.querySelectorAll('circle');
    circles.forEach(circle => {
        graph.removeChild(circle);
    });
}

function drawPoint(x, y, color) {
    const newPoint = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
    newPoint.setAttribute('cx', x);
    newPoint.setAttribute('cy', y);
    newPoint.setAttribute('fill', color);
    newPoint.setAttribute('r', "5");

    graph.appendChild(newPoint);
}

function randomHexColor() {
    const hex = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"]

    return "#" + hex[getRandomInt(15)] + hex[getRandomInt(15)]
        + hex[getRandomInt(15)] + hex[getRandomInt(15)]
        + hex[getRandomInt(15)] + hex[getRandomInt(15)];
}

function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

function redrawPoints(data) {
    refreshCircles();

    const xList = data.map((point) => point.x);
    const yList = data.map((point) => point.y);
    const checkList = data.map((point) => point.inArea);

    for (let i = 0; i < xList.length; i++) {
        const x = 200 + xList[i] * 36;
        const y = 200 - yList[i] * 36;

        if (checkList[i]) {
            drawPoint(x, y, "#00FF00");
        } else {
            drawPoint(x, y, "#FF0000");
        }
    }
}

document.addEventListener('DOMContentLoaded', function() {
    graph = document.getElementById("blueGraph");
    redrawGraph(1);
    graph.addEventListener('click', async function () {
        const rect = graph.getBoundingClientRect();
        var rad = document.getElementById('myForm:hiddenR').value;

        const mouseX = event.clientX - rect.x;
        const mouseY = event.clientY - rect.y;

        console.log(mouseX + ", " + mouseY);

        const centerX = graph.getAttribute("width") / 2;
        const centerY = graph.getAttribute("height") / 2;

        const x = (mouseX - centerX) / 36;
        const y = (centerY - mouseY) / 36;

        addPoint(
            [
                { name: "x", value: x.toString() },
                { name: "y", value: y.toString() },
                { name: "r", value: rad.toString() }
            ]
        )

        redrawReq();
    });
    }, false)

