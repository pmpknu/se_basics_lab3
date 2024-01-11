function updateClock() {
    const now = new Date();

    const hours = now.getHours() % 12;
    const minutes = now.getMinutes();
    const seconds = now.getSeconds();

    const hourAngle = (360 / 12) * hours + (360 / 12) * (minutes / 60);
    const minuteAngle = (360 / 60) * minutes + (360 / 60) * (seconds / 60);
    const secondAngle = (360 / 60) * seconds;

    document.getElementById('hourHand').setAttribute('transform', `rotate(${hourAngle} 100 100)`);
    document.getElementById('minuteHand').setAttribute('transform', `rotate(${minuteAngle} 100 100)`);
    document.getElementById('secondHand').setAttribute('transform', `rotate(${secondAngle} 100 100)`);
}

setInterval(updateClock, 12000);
updateClock();