"""Compile and package the preserved Java desktop source, using only JDK tools."""
from pathlib import Path
import os
import subprocess
import sys

root = Path(__file__).resolve().parents[1]
os.chdir(root)
classes = root / 'build/java/classes'
classes.mkdir(parents=True, exist_ok=True)
sources = sorted((root / 'Ricochet Robots/src').rglob('*.java'))
subprocess.run(['javac', '-encoding', 'UTF-8', '-d', str(classes), *map(str, sources)], check=True)
resources = root / 'Ricochet Robots/img'
subprocess.run(['jar', '--create', '--file', 'build/ricochet-robots.jar', '--main-class', 'controller.Driver', '-C', str(classes), '.', '-C', str(resources), '.'], check=True)
if '--test' in sys.argv:
    tests = root / 'build/java/tests'
    tests.mkdir(parents=True, exist_ok=True)
    subprocess.run(['javac', '-cp', str(classes), '-d', str(tests), 'tests/java/view/RulesTest.java'], check=True)
    subprocess.run(['java', '-Djava.awt.headless=true', '-cp', os.pathsep.join([str(classes), str(tests)]), 'view.RulesTest'], check=True)
print('Built build/ricochet-robots.jar from the preserved desktop source and resources.')
