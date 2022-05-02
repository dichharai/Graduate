class WrongNumberOfPlayersError < StandardError ; end
class NoSuchStrategyError < StandardError ; end

def rps_game_winner(game)
  raise WrongNumberOfPlayersError unless game.length == 2
  # YOUR CODE HERE
end

def rps_tournament_winner(tournament)
  # YOUR CODE HERE
end

#feel free to add your own helper functions as needed
